package com.supercoding.hackathon01.repository;

import com.supercoding.hackathon01.entity.Home;
import com.supercoding.hackathon01.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, Long> {
    List<Picture> findByHome(Home home);
    List<Picture> findByHome_Id(Long id);
}
