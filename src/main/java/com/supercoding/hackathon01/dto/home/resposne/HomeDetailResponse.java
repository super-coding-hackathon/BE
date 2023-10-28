package com.supercoding.hackathon01.dto.home.resposne;

import com.supercoding.hackathon01.entity.Address;
import com.supercoding.hackathon01.entity.Home;
import com.supercoding.hackathon01.entity.Picture;
import com.supercoding.hackathon01.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
    private String name;
    private Integer squareFeet;
    private Integer floor;
    private Long maintenanceFee;
    private Boolean isParking;
    private String createdAt;
    private String address;
    private String detailAddress;
    private String roadAddress;
    private List<ImageFiles> imageFiles;
    private String type;


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class ImageFiles {
        private String imageUrl;
        private Boolean isThumbnail;

        public static ImageFiles from(Picture picture) {
            return ImageFiles.builder()
                    .imageUrl(picture.getUrl())
                    .isThumbnail(picture.getIsThumbnail())
                    .build();
        }

    }

    public static HomeDetailResponse of(Home home, Address address) {
        return HomeDetailResponse.builder()
                .homeId(home.getId())
                .transactionType(home.getTransactionType())
                .seller(home.getUser().getNickname())
                .deposit(home.getDeposit())
                .price(home.getPrice())
                .name(home.getName())
                .squareFeet(home.getSquareFeet())
                .floor(home.getFloor())
                .maintenanceFee(home.getMaintenanceFee())
                .isParking(home.getIsParking())
                .createdAt(DateUtils.convertToString(home.getCreatedAt()))
                .address(address.getAddress())
                .detailAddress(address.getDetailAddress())
                .roadAddress(address.getRoadAddress())
                .imageFiles(home.getPictures().stream().map(ImageFiles::from).collect(Collectors.toList()))
                .type(home.getCategory().getName())
                .build();
    }

}
