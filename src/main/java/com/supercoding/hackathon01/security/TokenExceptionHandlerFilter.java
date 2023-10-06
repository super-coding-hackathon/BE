package com.supercoding.hackathon01.security;

import com.supercoding.hackathon01.error.CustomException;
import com.supercoding.hackathon01.error.ErrorCode;
import com.supercoding.hackathon01.error.domain.UserErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            setErrorResponse(UserErrorCode.CANT_ACCESS);
        } catch (AccessDeniedException e) {
            setErrorResponse(UserErrorCode.HANDLE_ACCESS_DENIED);
        } catch (JwtException e) {
            filterChain.doFilter(request, response);
        }
    }

    private void setErrorResponse(ErrorCode errorCode) {
        throw new CustomException(errorCode);
    }

}
