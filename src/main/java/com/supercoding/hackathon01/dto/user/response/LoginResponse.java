package com.supercoding.hackathon01.dto.user.response;

import com.supercoding.hackathon01.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String email;
    private String nickname;
    private String accessToken;

    public static LoginResponse from(User user, String accessToken) {
        return LoginResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .accessToken(accessToken)
                .build();
    }

}
