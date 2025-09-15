package com.priceshoes.academy.repository;

import com.priceshoes.academy.domain.CustomerCourseChapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CustomerCourseChapterRepository extends JpaRepository<CustomerCourseChapter, Long> {
    List<CustomerCourseChapter> findByStatusAndChapter_Course_id_AndCustomerCourse_CustomerId(CustomerCourseChapter.CustomerCourseChapterStatus status, Long courseId, String customerId);



}
