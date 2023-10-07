package com.supercoding.hackathon01.dto.home.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class RegisterHomeRequest {

    @ApiModelProperty(value = "카테고리 (1: 아파트, 2: 빌라, 3: 원룸)", dataType = "Long")
    private Long categoryId;
    @ApiModelProperty(value = "전세 or 매매", dataType = "String")
    private String transactionType;
    @ApiModelProperty(value = "보증금", dataType = "Long")
    private Long deposit;
    @ApiModelProperty(value = "가격", dataType = "Long")
    private Long price;
    @ApiModelProperty(value = "평수", dataType = "Integer")
    private Integer squareFeet;
    @ApiModelProperty(value = "층수", dataType = "Integer")
    private Integer floor;
    @ApiModelProperty(value = "관리비", dataType = "Long")
    private Long maintenanceFee;
    @ApiModelProperty(value = "주차여부", dataType = "Boolean")
    private Boolean isParking;
    @ApiModelProperty(value = "상세주소", dataType = "String")
    private String detailAddress;
    @ApiModelProperty(value = "주소", dataType = "String")
    private String address;
    @ApiModelProperty(value = "위도(소수점 6자리)", dataType = "Double")
    private Double latitude;
    @ApiModelProperty(value = "경도(소수점 6자리)", dataType = "Double")
    private Double longitude;
    @ApiModelProperty(value = "카카오 map Id", dataType = "String")
    private String mapId;
    @ApiModelProperty(value = "도로명 주소", dataType = "String")
    private String roadAddress;

}
