package com.supercoding.hackathon01.dto.my_page.response;

import com.supercoding.hackathon01.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeListResponse {

    private Long homeId;
    private String homeName;
    private String categoryName;
    private String transactionType;
    private String address;
    private String thumbnailUrl;
    private String createdAt;

    public HomeListResponse(Long homeId, String homeName, String categoryName, String transactionType, String address, String thumbnailUrl, Instant createdAt) {
        this.homeId = homeId;
        this.homeName = homeName;
        this.categoryName = categoryName;
        this.transactionType = transactionType;
        this.address = address;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = DateUtils.convertToString(createdAt);
    }
}
