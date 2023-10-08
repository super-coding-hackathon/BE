package com.supercoding.hackathon01.dto.my_page.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellingListResponse {
    private Long transactionId;
    private String buyer;
    private String transactionStatus;
    private Long homeId;
    private String homeName;
    private String buyerContractFile;
    private String address;
    private String thumbnailUrl;
    private Long deposit;
}
