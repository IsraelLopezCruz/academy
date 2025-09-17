package com.priceshoes.academy.repository;

import com.priceshoes.academy.domain.Course;
import com.priceshoes.academy.domain.CustomerCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerCourseRepository extends JpaRepository<CustomerCourse, Long> {

    @Query(
            "select c from CustomerCourse c where c.customerId = :customerId " +
                    "AND c.course.status = :status "
    )
    List<CustomerCourse> findByCustomerIdAndCourseStatus(@Param("customerId") String customerId, @Param("status") Course.CourseStatus status);

    @Query(
            "select c from CustomerCourse c where c.customerId = :customerId " +
                    "AND c.course.id = :courseId "
    )
    Optional<CustomerCourse> findByCustomerIdAndCourseId(@Param("customerId") String customerId, @Param("courseId") Long courseId);
    long countByStatus(CustomerCourse.CustomerCourseStatus status);
    long countByStatusAndCourseId(CustomerCourse.CustomerCourseStatus status, @Param("courseId") Long courseId);
    List<CustomerCourse> findByStatusAndCustomerId(CustomerCourse.CustomerCourseStatus status, @Param("customerId") String customerId);
    List<CustomerCourse> findByCustomerId(String customerId);

    @Query("SELECT DISTINCT cc.course.id FROM CustomerCourse cc")
    List<Long> findDistinctCourse_Id();

}
