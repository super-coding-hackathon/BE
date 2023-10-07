package com.supercoding.hackathon01.repository;

import com.supercoding.hackathon01.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
