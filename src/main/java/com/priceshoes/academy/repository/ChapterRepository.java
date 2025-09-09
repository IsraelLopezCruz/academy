package com.priceshoes.academy.repository;

import com.priceshoes.academy.domain.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    List<Chapter> findByCourse_Id(Long courseId);
}
