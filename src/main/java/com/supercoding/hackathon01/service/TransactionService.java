package com.supercoding.hackathon01.service;

import com.supercoding.hackathon01.dto.my_page.response.HomeListResponse;
import com.supercoding.hackathon01.dto.my_page.response.PurchaseListResponse;
import com.supercoding.hackathon01.dto.my_page.response.SellingListResponse;
import com.supercoding.hackathon01.dto.my_page.response.StatusCountResponse;
import com.supercoding.hackathon01.dto.transaction.request.NextStepRequest;
import com.supercoding.hackathon01.dto.vo.PaginationResponse;
import com.supercoding.hackathon01.entity.Home;
import com.supercoding.hackathon01.entity.Status;
import com.supercoding.hackathon01.entity.Transaction;
import com.supercoding.hackathon01.entity.User;
import com.supercoding.hackathon01.enums.FilePath;
import com.supercoding.hackathon01.error.CustomException;
import com.supercoding.hackathon01.error.domain.FileErrorCode;
import com.supercoding.hackathon01.error.domain.HomeErrorCode;
import com.supercoding.hackathon01.error.domain.TransactionErrorCode;
import com.supercoding.hackathon01.error.domain.UserErrorCode;
import com.supercoding.hackathon01.repository.UserRepository;
import com.supercoding.hackathon01.repository.home.HomeRepository;
import com.supercoding.hackathon01.repository.transaction.TransactionRepository;
import com.supercoding.hackathon01.security.AuthHolder;
import com.supercoding.hackathon01.utils.PaginationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final HomeRepository homeRepository;
    private final AwsS3Service awsS3Service;

    public Void requestTransaction(Long homeId) {
        User user = validUser(AuthHolder.getUserId());
        Home home = validHome(homeId);
        Transaction transaction = Transaction.of(home, user);
        transactionRepository.save(transaction);
        return null;
    }

    public PaginationResponse<SellingListResponse> getSellingList(Pageable pageable) {
        User user = validUser(AuthHolder.getUserId());
        Page<SellingListResponse> pageData = transactionRepository.findBySellingList(user, pageable);
        return new PaginationBuilder<SellingListResponse>()
                .hasNext(pageData.hasNext())
                .hasPrivious(pageData.hasPrevious())
                .totalPages(pageData.getTotalPages())
                .contents(pageData.getContent())
                .totalElements(pageData.getTotalElements())
                .build();
    }

    public PaginationResponse<PurchaseListResponse> getPurchaseList(Pageable pageable) {
        User user = validUser(AuthHolder.getUserId());
        Page<PurchaseListResponse> pageData = transactionRepository.findByPurchaseList(user, pageable);
        return new PaginationBuilder<PurchaseListResponse>()
                .hasNext(pageData.hasNext())
                .hasPrivious(pageData.hasPrevious())
                .totalPages(pageData.getTotalPages())
                .contents(pageData.getContent())
                .totalElements(pageData.getTotalElements())
                .build();
    }


    public PaginationResponse<HomeListResponse> getMyHomeList(Pageable pageable) {
        User user = validUser(AuthHolder.getUserId());
        Page<HomeListResponse> pageData = transactionRepository.findByMyHomeList(user, pageable);
        return new PaginationBuilder<HomeListResponse>()
                .hasNext(pageData.hasNext())
                .hasPrivious(pageData.hasPrevious())
                .totalPages(pageData.getTotalPages())
                .contents(pageData.getContent())
                .totalElements(pageData.getTotalElements())
                .build();
    }

    public List<StatusCountResponse> getMySellingStatusCount() {
        User user = validUser(AuthHolder.getUserId());
        return transactionRepository.countTransactionsByStatusForSeller(user);
    }

    public List<StatusCountResponse> getMyPurchaseStatusCount() {
        User user = validUser(AuthHolder.getUserId());
        return transactionRepository.countTransactionsByStatusForBuyer(user);
    }

    public Void nextStep(Long transactionId, NextStepRequest nextStepRequest) {
        User user = validUser(AuthHolder.getUserId());
        Transaction transaction = validTransaction(transactionId);

        switch (transaction.getStatus().getName()) {

            case "거래신청":
                checkSeller(user, transaction);
                startTransaction(nextStepRequest, transaction);
                break;
            case "거래승인":
                checkBuyer(user, transaction);
                approveContract(nextStepRequest, transaction);
                break;
            case "계약검토":
                checkSeller(user, transaction);
                reviewContract(nextStepRequest, transaction);
                break;
            case "이체대기":
                checkBuyer(user, transaction);
                waitForTransfer(transaction);
                break;
            case "이체검토":
                checkSeller(user, transaction);
                completeTransaction(transaction);
                break;
            default:
                break;

        }

        return null;
    }

    private void startTransaction(NextStepRequest nextStepRequest, Transaction transaction) {
        // 거래신청 스텝의 로직 처리
        // 판매자 계약서를 첨부하고 API 호출
        if (nextStepRequest.getSellerContractFile() == null) throw new CustomException(TransactionErrorCode.NOT_FOUND_SELLER_CONTRACT_FILE);
        transaction.setSellerFileUrl(uploadImageFile(nextStepRequest.getSellerContractFile(), transaction));
        transaction.setStatus(new Status(2L, "거래승인"));
        transactionRepository.save(transaction);
    }

    private void approveContract(NextStepRequest nextStepRequest, Transaction transaction) {
        // 거래승인 스텝의 로직 처리
        // 구매자 계약서를 첨부하고 API 호출
        if (nextStepRequest.getBuyerContractFile() == null) throw new CustomException(TransactionErrorCode.NOT_FOUND_BUYER_CONTRACT_FILE);
        transaction.setBuyerFileUrl(uploadImageFile(nextStepRequest.getBuyerContractFile(), transaction));
        transaction.setStatus(new Status(3L, "계약검토"));
        transactionRepository.save(transaction);
    }

    private void reviewContract(NextStepRequest nextStepRequest, Transaction transaction) {
        // 계약검토 스텝의 로직 처리
        // 판매자가 계약서 확인 후 계좌번호를 담아 API 호출
        if (nextStepRequest.getAccountNumber() == null) throw new CustomException(TransactionErrorCode.NOT_FOUND_ACCOUNT_NUMBER);
        transaction.setAccountNumber(nextStepRequest.getAccountNumber());
        transaction.setStatus(new Status(4L, "이체대기"));
        transactionRepository.save(transaction);
        
    }

    private void waitForTransfer(Transaction transaction) {
        // 이체대기 스텝의 로직 처리
        // 구매자가 계좌번호에 송금 후 API 호출
        transaction.setStatus(new Status(5L, "이체검토"));
        transactionRepository.save(transaction);
    }

    private void completeTransaction(Transaction transaction) {
        // 거래완료 스텝의 로직 처리
        transaction.setStatus(new Status(6L, "거래완료"));
        transactionRepository.save(transaction);
    }


    private User validUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));
    }

    private Home validHome(Long homeId) {
        return homeRepository.findById(homeId).orElseThrow(() -> new CustomException(HomeErrorCode.NOT_FOUND_HOME));
    }

    private Transaction validTransaction(Long transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow(() -> new CustomException(TransactionErrorCode.NOT_FOUND));
    }

    private void checkSeller(User user, Transaction transaction){
        if (!Objects.equals(user.getId(), transaction.getSeller().getId()))
            throw new CustomException(TransactionErrorCode.FORBIDDEN_SELLER);
    }

    private void checkBuyer(User user, Transaction transaction){
        if (!Objects.equals(user.getId(), transaction.getBuyer().getId()))
            throw new CustomException(TransactionErrorCode.FORBIDDEN_BUYER);
    }

    private String uploadImageFile(MultipartFile multipartFile, Transaction transaction) {
        String uniqueIdentifier = UUID.randomUUID().toString();
        try {
            if (multipartFile != null) {
                return awsS3Service.uploadImage(multipartFile, FilePath.CONTRACT_DIR.getPath() + transaction.getId() + "/" + uniqueIdentifier);
            }
        }catch (IOException e) {
            throw new CustomException(FileErrorCode.FILE_UPLOAD_FAILED);
        }
        return null;
    }
}
