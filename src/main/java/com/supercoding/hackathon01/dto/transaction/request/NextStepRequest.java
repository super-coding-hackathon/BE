package com.supercoding.hackathon01.dto.transaction.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NextStepRequest {

    private String accountNumber;

    @JsonIgnore
    @Setter
    private MultipartFile sellerContractFile;
    @JsonIgnore
    @Setter
    private MultipartFile buyerContractFile;

}
