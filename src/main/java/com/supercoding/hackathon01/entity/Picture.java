package com.supercoding.hackathon01.entity;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "picture")
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "home_id")
    private Home home;

    @Lob
    @Column(name = "url")
    private String url;

    @Column(name = "is_thumbnail")
    private Boolean isThumbnail;

    public static Picture of(String url, Home home, Boolean isThumbnail) {
        return Picture.builder()
                .home(home)
                .url(url)
                .isThumbnail(isThumbnail)
                .build();
    }

}