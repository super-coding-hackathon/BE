package com.supercoding.hackathon01.service;

import com.supercoding.hackathon01.dto.home.request.RegisterHomeRequest;
import com.supercoding.hackathon01.entity.*;
import com.supercoding.hackathon01.error.CustomException;
import com.supercoding.hackathon01.error.domain.HomeErrorCode;
import com.supercoding.hackathon01.error.domain.UserErrorCode;
import com.supercoding.hackathon01.repository.AddressRepository;
import com.supercoding.hackathon01.repository.CategoryRepository;
import com.supercoding.hackathon01.repository.UserRepository;
import com.supercoding.hackathon01.repository.home.HomeRepository;
import com.supercoding.hackathon01.security.AuthHolder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class HomeTestDataService {

    @Autowired
    private HomeService homeService;

    @Autowired
    private HomeRepository homeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AddressRepository addressRepository;


    @Test
    public void testSaveRandomLoc() {
        double baseLatitude = 37.49808633653005; // 기준 위도
        double baseLongitude = 127.02800140627488; // 기준 경도
        int numberOfLocations = 10000; // 생성할 데이터 수

        User user = userRepository.findById(8L).orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));
        for (int i = 1; i < numberOfLocations; i++) {
            Random categoryRandom = new Random();
            Random depositRandom = new Random();
            Random priceRandom = new Random();
            Random squareFeetRandom = new Random();
            Random floorRandom = new Random();
            Random maintenanceRandom = new Random();
            Random booleanRandom = new Random();

            RegisterHomeRequest homeRequest = new RegisterHomeRequest();
            homeRequest.setCategoryId((long) (categoryRandom.nextInt(3)+1));
            if (i % 2 == 0) {
                homeRequest.setTransactionType("매매");
            } else homeRequest.setTransactionType("전세");
            homeRequest.setDeposit((long) (depositRandom.nextInt(9999) + 1));
            homeRequest.setPrice((long) (priceRandom.nextInt(9999) + 1));
            homeRequest.setSquareFeet((squareFeetRandom.nextInt(120) + 1));
            homeRequest.setFloor((floorRandom.nextInt(20) + 1));
            homeRequest.setMaintenanceFee((long) (maintenanceRandom.nextInt(9999) + 1));
            homeRequest.setIsParking(booleanRandom.nextBoolean());
            homeRequest.setAddress("강남역 테스트 주소 데이터 "+ i + " 번째");
            homeRequest.setRoadAddress("강남역 테스트 도로명 주소 데이터 "+ i + " 번째");
            homeRequest.setName("강남역 테스트 건물 이름 "+ i + " 번째");
            homeRequest.setLatitude(baseLatitude + Math.random() * 0.027);
            homeRequest.setLongitude(baseLongitude + Math.random() * 0.027);

            registerHome(homeRequest, user);
        }
    }

    public void registerHome(RegisterHomeRequest homeRequest, User user) {

        Category category = categoryRepository.findById(homeRequest.getCategoryId()).orElseThrow(() -> new CustomException(HomeErrorCode.NOT_FOUND_CATEGORY));
        Home home = Home.of(homeRequest, user, category);
        homeRepository.save(home);
        Address address = Address.of(homeRequest, home);
        addressRepository.save(address);
    }

}
