package com.supercoding.hackathon01.dto.my_page.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseListResponse {
    private Long transactionId;
    private String seller;
    private String transactionStatus;
    private Long transactionStatusId;
    private Long homeId;
    private String homeName;
    private String sellerContractFile;
    private String address;
    private String thumbnailUrl;
    private Long deposit;
    private String accountNumber;
}
