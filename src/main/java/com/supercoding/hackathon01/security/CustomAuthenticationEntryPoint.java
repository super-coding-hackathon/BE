package com.supercoding.hackathon01.security;

import com.supercoding.hackathon01.error.CustomException;
import com.supercoding.hackathon01.error.domain.UserErrorCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        throw new CustomException(UserErrorCode.HANDLE_ACCESS_DENIED);
    }
}
