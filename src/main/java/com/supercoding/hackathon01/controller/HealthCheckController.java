package com.supercoding.hackathon01.controller;

import com.supercoding.hackathon01.dto.vo.Response;
import com.supercoding.hackathon01.utils.ApiUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health-check/")
public class HealthCheckController {

    @GetMapping("")
    public Response<Void> checkServerStatus() {
        return ApiUtils.success(HttpStatus.OK, "서버가 살아있음", null);
    }

}
