package com.supercoding.hackathon01.repository.home;

import com.supercoding.hackathon01.dto.home.request.ViewHomeListRequest;
import com.supercoding.hackathon01.dto.home.resposne.ViewPointListResponse;
import com.supercoding.hackathon01.entity.Address;
import com.supercoding.hackathon01.entity.Home;

import java.util.List;

public interface HomeCustomRepository {

    List<Home> findHomeList(ViewHomeListRequest viewHomeListRequest);

    List<ViewPointListResponse> findPointList(ViewHomeListRequest viewHomeListRequest);
}
