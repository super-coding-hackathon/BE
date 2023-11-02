package com.supercoding.hackathon01.error.domain;

import com.supercoding.hackathon01.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HomeErrorCode implements ErrorCode {

    FILE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST.value(), "파일 업로드 실패"),
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND.value(), "정상 범위의 카테고리를 입력해주세요"),
    NOT_FOUND_HOME(HttpStatus.NOT_FOUND.value(), "집을 찾을 수 없습니다"),
    FORBIDDEN_HOME(HttpStatus.FORBIDDEN.value(), "내가 등록 한 집이 아닙니다"),
    NOT_FOUND_SORT_TYPE(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 정렬 타입입니다."),;


    private final int code;
    private final String message;


}