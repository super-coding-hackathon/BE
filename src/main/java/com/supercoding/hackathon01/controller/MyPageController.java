package com.supercoding.hackathon01.controller;

import com.supercoding.hackathon01.dto.my_page.response.HomeListResponse;
import com.supercoding.hackathon01.dto.my_page.response.PurchaseListResponse;
import com.supercoding.hackathon01.dto.my_page.response.SellingListResponse;
import com.supercoding.hackathon01.dto.my_page.response.StatusCountResponse;
import com.supercoding.hackathon01.dto.user.response.MyInfoResponse;
import com.supercoding.hackathon01.dto.vo.PaginationResponse;
import com.supercoding.hackathon01.dto.vo.Response;
import com.supercoding.hackathon01.security.Auth;
import com.supercoding.hackathon01.service.TransactionService;
import com.supercoding.hackathon01.service.UserService;
import com.supercoding.hackathon01.utils.ApiUtils;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/my-page")
@Api(tags = "마이페이지 API")
public class MyPageController {

    private final UserService userService;
    private final TransactionService transactionService;

    @Auth
    @GetMapping("")
    @Operation(summary = "내 정보 조회 API", description = "")
    public Response<MyInfoResponse> getMyInfo() {
        return ApiUtils.success(HttpStatus.OK, userService.getMyInfo());
    }

    @Auth
    @GetMapping("/sell")
    @Operation(summary = "판매 현황 조회")
    public Response<PaginationResponse<SellingListResponse>> getSellingList(Pageable pageable) {
        return ApiUtils.success(HttpStatus.OK, transactionService.getSellingList(pageable));
    }

    @Auth
    @GetMapping("/sell/detail")
    @Operation(summary = "판매 현황 조회")
    public Response<SellingListResponse> getSellingDetail(Long transactionId) {
        return ApiUtils.success(HttpStatus.OK, transactionService.getSellingDetail(transactionId));
    }

    @Auth
    @GetMapping("/buy")
    @Operation(summary = "구매 현황 조회")
    public Response<PaginationResponse<PurchaseListResponse>> getPurchaseList(Pageable pageable) {
        return ApiUtils.success(HttpStatus.OK, transactionService.getPurchaseList(pageable));
    }

    @Auth
    @GetMapping("/buy/detail")
    @Operation(summary = "구매 현황 조회")
    public Response<PurchaseListResponse> getPurchaseDetail(Long transactionId) {
        return ApiUtils.success(HttpStatus.OK, transactionService.getPurchaseDetail(transactionId));
    }

    @Auth
    @GetMapping("/home")
    @Operation(summary = "내가 올린 집 리스트 조회")
    public Response<PaginationResponse<HomeListResponse>> getHomeList(Pageable pageable) {
        return ApiUtils.success(HttpStatus.OK, transactionService.getMyHomeList(pageable));
    }

    @Auth
    @GetMapping("/sell/status-count")
    @Operation(summary = "판매 현황 상태 개수 조회")
    public Response<List<StatusCountResponse>> getSellingStatusCount() {
        return ApiUtils.success(HttpStatus.OK, transactionService.getMySellingStatusCount());
    }

    @Auth
    @GetMapping("/buy/status-count")
    @Operation(summary = "구매 현황 상태 개수 조회")
    public Response<List<StatusCountResponse>> getPurchaseStatusCount() {
        return ApiUtils.success(HttpStatus.OK, transactionService.getMyPurchaseStatusCount());
    }



}
