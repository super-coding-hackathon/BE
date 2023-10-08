package com.supercoding.hackathon01.dto.my_page.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatusCountResponse {
    private String statusName;
    private Long count;
}
