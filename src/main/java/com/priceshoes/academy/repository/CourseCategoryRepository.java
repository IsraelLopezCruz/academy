package com.priceshoes.academy.repository;

import com.priceshoes.academy.domain.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Long> {

    List<CourseCategory> findByCategory_Id(Long categoryId);
    List<CourseCategory> findByCategory_IdAndPriorityGreaterThanEqual(Long categoryId, Integer priority);
    Optional<CourseCategory> findByCourse_IdAndCategory_Id(Long courseId, Long categoryId);
}
