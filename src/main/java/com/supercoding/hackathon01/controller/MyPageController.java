package com.supercoding.hackathon01.controller;

import com.supercoding.hackathon01.dto.my_page.response.HomeListResponse;
import com.supercoding.hackathon01.dto.my_page.response.PurchaseListResponse;
import com.supercoding.hackathon01.dto.my_page.response.SellingListResponse;
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
    @GetMapping("/buy")
    @Operation(summary = "구매 현황 조회")
    public Response<PaginationResponse<PurchaseListResponse>> getPurchaseList(Pageable pageable) {
        return ApiUtils.success(HttpStatus.OK, transactionService.getPurchaseList(pageable));
    }

    @Auth
    @GetMapping("/home")
    @Operation(summary = "내가 올린 집 리스트 조회")
    public Response<PaginationResponse<HomeListResponse>> getHomeList(Pageable pageable) {
        return ApiUtils.success(HttpStatus.OK, transactionService.getMyHomeList(pageable));
    }


}
