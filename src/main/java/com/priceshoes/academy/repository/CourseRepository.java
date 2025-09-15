package com.priceshoes.academy.repository;

import com.priceshoes.academy.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByStatusInAndIdNotIn(List<Course.CourseStatus> status, List<Long> ids);
    List<Course> findByIdNotIn(List<Long> ids);
}
