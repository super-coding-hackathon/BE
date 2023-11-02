package com.supercoding.hackathon01.dto.home.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class ViewHomeListRequest {

    private Long categoryId;
    private Double latitude;
    private Double longitude;
    private Integer squareFeetFilter;
    private Integer priceFilter;
    private Boolean isParking;

}
