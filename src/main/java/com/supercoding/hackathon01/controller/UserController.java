package com.supercoding.hackathon01.controller;

import com.supercoding.hackathon01.dto.user.request.LoginRequest;
import com.supercoding.hackathon01.dto.user.request.SignupRequest;
import com.supercoding.hackathon01.dto.user.response.LoginResponse;
import com.supercoding.hackathon01.dto.vo.Response;
import com.supercoding.hackathon01.service.UserService;
import com.supercoding.hackathon01.utils.ApiUtils;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
@Api(tags = "회원및 로그인 API")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/signup")
    @Operation(summary = "회원 가입 API")
    public Response<LoginResponse> signup(@RequestBody SignupRequest signupRequest) {

        return ApiUtils.success(HttpStatus.CREATED, userService.signup(signupRequest));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "")
    public Response<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ApiUtils.success(HttpStatus.OK, userService.login(loginRequest));
    }

}
