package com.supercoding.hackathon01.error.domain;

import com.supercoding.hackathon01.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TransactionErrorCode implements ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "거레번호가 존재하지 않습니다"),
    NOT_FOUND_SELLER_CONTRACT_FILE(HttpStatus.BAD_REQUEST.value(), "판매자 계약서를 첨부해주세요."),
    NOT_FOUND_BUYER_CONTRACT_FILE(HttpStatus.BAD_REQUEST.value(), "구매자 계약서를 첨부해주세요."),
    NOT_FOUND_ACCOUNT_NUMBER(HttpStatus.BAD_REQUEST.value(), "계좌번호가 없습니다."),
    FORBIDDEN_SELLER(HttpStatus.FORBIDDEN.value(), "판매자만 거래를 진행할 수 있습니다."),
    FORBIDDEN_BUYER(HttpStatus.FORBIDDEN.value(), "구매자만 거래를 진행할 수 있습니다.")
    ;


    private final int code;
    private final String message;

}
