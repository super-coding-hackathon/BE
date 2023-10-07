package com.supercoding.hackathon01.service;

import com.supercoding.hackathon01.dto.my_page.response.HomeListResponse;
import com.supercoding.hackathon01.dto.my_page.response.PurchaseListResponse;
import com.supercoding.hackathon01.dto.my_page.response.SellingListResponse;
import com.supercoding.hackathon01.dto.vo.PaginationResponse;
import com.supercoding.hackathon01.entity.Home;
import com.supercoding.hackathon01.entity.Transaction;
import com.supercoding.hackathon01.entity.User;
import com.supercoding.hackathon01.error.CustomException;
import com.supercoding.hackathon01.error.domain.HomeErrorCode;
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

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final HomeRepository homeRepository;

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


    private User validUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));
    }

    private Home validHome(Long homeId) {
        return homeRepository.findById(homeId).orElseThrow(() -> new CustomException(HomeErrorCode.NOT_FOUND_HOME));
    }

}
