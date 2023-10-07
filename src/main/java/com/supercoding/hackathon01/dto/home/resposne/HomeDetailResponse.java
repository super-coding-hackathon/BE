package com.supercoding.hackathon01.dto.home.resposne;

import com.supercoding.hackathon01.entity.Address;
import com.supercoding.hackathon01.entity.Home;
import com.supercoding.hackathon01.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeDetailResponse {

    private Long homeId;
    private String transactionType;
    private String seller;
    private Long deposit;
    private Long price;
    private Integer squareFeet;
    private Integer floor;
    private Long maintenanceFee;
    private Boolean isParking;
    private String createdAt;
    private String address;
    private String detailAddress;
    private String roadAddress;

    public static HomeDetailResponse of(Home home, Address address) {
        return HomeDetailResponse.builder()
                .homeId(home.getId())
                .transactionType(home.getTransactionType())
                .seller(home.getUser().getNickname())
                .deposit(home.getDeposit())
                .price(home.getPrice())
                .squareFeet(home.getSquareFeet())
                .floor(home.getFloor())
                .maintenanceFee(home.getMaintenanceFee())
                .isParking(home.getIsParking())
                .createdAt(DateUtils.convertToString(home.getCreatedAt()))
                .address(address.getAddress())
                .detailAddress(address.getDetailAddress())
                .roadAddress(address.getRoadAddress())
                .build();
    }

}
