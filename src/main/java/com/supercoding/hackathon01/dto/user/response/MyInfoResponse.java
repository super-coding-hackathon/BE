package com.supercoding.hackathon01.dto.user.response;

import com.supercoding.hackathon01.entity.User;
import com.supercoding.hackathon01.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyInfoResponse {

    private String email;
    private String nickname;
    private String createdAt;
    private String phoneNumber;

    public static MyInfoResponse from(User user) {
        return MyInfoResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(DateUtils.convertToString(user.getCreatedAt()))
                .build();
    }
}
