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
public class ViewPointListResponse {

    private Long homeId;
    private Double latitude;
    private Double longitude;
    private String transactionType;
    private Long deposit;
    private Long price;

    public static ViewPointListResponse from(Address address) {
        return ViewPointListResponse.builder()
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .build();
    }

}
