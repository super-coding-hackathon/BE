package com.supercoding.hackathon01.repository.home;

import com.supercoding.hackathon01.entity.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface HomeRepository extends JpaRepository<Home, Long>, HomeCustomRepository {
    @Transactional
    @Modifying
    @Query("update Home h set h.isDeleted = true where h.id = ?1")
    int updateIsDeletedById(Long id);
}
