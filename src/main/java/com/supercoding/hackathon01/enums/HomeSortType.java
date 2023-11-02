package com.supercoding.hackathon01.enums;

import com.supercoding.hackathon01.error.CustomException;
import com.supercoding.hackathon01.error.domain.HomeErrorCode;

public enum HomeSortType {
    CREATED_DESC, //최신순
    PRICE_DESC, //가격 높은 순
    PRICE_ASC,   // 가격 낮은 순
    SQUARE_DESC,   // 면적 높은 순
    SQUARE_ASC;  // 면적 낮은 순

    public static HomeSortType fromString(String value) {
        try {
            if (value == null || "".equals(value)) value = "CREATED_AT";
            return HomeSortType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(HomeErrorCode.NOT_FOUND_SORT_TYPE);
        }
    }
}
