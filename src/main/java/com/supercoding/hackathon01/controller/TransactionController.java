package com.supercoding.hackathon01.controller;

import com.supercoding.hackathon01.dto.vo.Response;
import com.supercoding.hackathon01.security.Auth;
import com.supercoding.hackathon01.service.TransactionService;
import com.supercoding.hackathon01.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
