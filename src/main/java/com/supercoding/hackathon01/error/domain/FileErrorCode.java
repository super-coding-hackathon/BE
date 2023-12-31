package com.supercoding.hackathon01.error.domain;


import com.supercoding.hackathon01.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FileErrorCode implements ErrorCode {

    FILE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST.value(), "파일 업로드 실패");


    private final int code;
    private final String message;


}
