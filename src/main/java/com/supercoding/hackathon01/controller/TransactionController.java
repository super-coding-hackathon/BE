package com.supercoding.hackathon01.controller;

import com.supercoding.hackathon01.dto.transaction.request.NextStepRequest;
import com.supercoding.hackathon01.dto.vo.Response;
import com.supercoding.hackathon01.security.Auth;
import com.supercoding.hackathon01.service.TransactionService;
import com.supercoding.hackathon01.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @Auth
    @PostMapping("/{home_id}")
    @Operation(summary = "거래 요청", description = "거래를 요청합니다")
    public Response<Void> requestTransaction(@PathVariable("home_id") Long homeId) {
        return ApiUtils.success(HttpStatus.CREATED, transactionService.requestTransaction(homeId));
    }

    @Auth
    @PostMapping(value = "/{transaction_id}/next-step", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "거래 다음 스텝으로 진행")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {@Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
            schema = @Schema(implementation = MultipartFile.class)), @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NextStepRequest.class))})
    public Response<Void> nextStepTransaction(@PathVariable("transaction_id") Long transactionId,
                                              @RequestPart(required = false) NextStepRequest nextStepRequest,
                                              @RequestPart(required = false) MultipartFile buyerContractFile,
                                              @RequestPart(required = false) MultipartFile sellerContractFile
                                              ) {
        if (nextStepRequest == null) {
            nextStepRequest = new NextStepRequest();
        }
        nextStepRequest.setBuyerContractFile(buyerContractFile);
        nextStepRequest.setSellerContractFile(sellerContractFile);
        return ApiUtils.success(HttpStatus.OK, transactionService.nextStep(transactionId, nextStepRequest));
    }

}
