package com.supercoding.hackathon01.controller;

import com.supercoding.hackathon01.dto.user.response.MyInfoResponse;
import com.supercoding.hackathon01.dto.vo.Response;
import com.supercoding.hackathon01.security.Auth;
import com.supercoding.hackathon01.service.UserService;
import com.supercoding.hackathon01.utils.ApiUtils;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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

    @Auth
    @GetMapping("")
    @Operation(summary = "내 정보 조회 API", description = "")
    public Response<MyInfoResponse> getMyInfo() {
        return ApiUtils.success(HttpStatus.OK, userService.getMyInfo());
    }

}
