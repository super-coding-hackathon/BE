package com.supercoding.hackathon01.dto.home.resposne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewHomeListPageResponse {

    private List<HomeList> homeLists;
    private Long nextCursorId;
    private String nextCursorValue;

}
