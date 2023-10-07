package com.supercoding.hackathon01.entity;

import com.supercoding.hackathon01.dto.home.request.RegisterHomeRequest;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "home_id", nullable = false)
    private Home home;

    @Lob
    @Column(name = "detail_address")
    private String detailAddress;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Size(max = 45)
    @Column(name = "map_id", length = 45)
    private String mapId;

    @Size(max = 45)
    @Column(name = "road_address", length = 45)
    private String roadAddress;

    @NotNull
    @Lob
    @Column(name = "address", nullable = false)
    private String address;

    public static Address of(RegisterHomeRequest homeRequest, Home home) {
        return Address.builder()
                .address(homeRequest.getAddress())
                .detailAddress(homeRequest.getDetailAddress())
                .latitude(homeRequest.getLatitude())
                .longitude(homeRequest.getLongitude())
                .home(home)
                .mapId(homeRequest.getMapId())
                .roadAddress(homeRequest.getRoadAddress())
                .build();
    }

}