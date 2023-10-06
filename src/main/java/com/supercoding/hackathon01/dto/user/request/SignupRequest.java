package com.supercoding.hackathon01.dto.user.request;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
@Schema(description = "회원가입 요청 DTO")
public class SignupRequest {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordCheck;
    @NotBlank
    private String nickname;
    @NotBlank
    private String phoneNumber;

}
