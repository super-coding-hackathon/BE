package com.supercoding.hackathon01.dto.home.resposne;

import com.supercoding.hackathon01.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeList {

    private Long homeId;
    private String name;
    private String transactionType;
    private Long deposit;
    private Long price;
    private Integer squareFeet;

}