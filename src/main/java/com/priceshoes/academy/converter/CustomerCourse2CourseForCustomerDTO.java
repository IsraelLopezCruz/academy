package com.priceshoes.academy.converter;

import com.priceshoes.academy.domain.CustomerCourse;
import com.priceshoes.academy.service.dto.CategoryDTO;
import com.priceshoes.academy.service.dto.CourseForCustomerDTO;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@AllArgsConstructor
public class CustomerCourse2CourseForCustomerDTO implements Function<CustomerCourse, CourseForCustomerDTO> {

    @Override
    public CourseForCustomerDTO apply(@NonNull CustomerCourse customerCourse) {
        var course = customerCourse.getCourse();
        var lsCategory = course.getCourseCategory().stream().map(cc -> CategoryDTO.builder()
                .categoryId(cc.getCategoryId())
                .title(cc.getCategory().getTitle())
                .priority(cc.getCategory().getPriority()).build()).toList();

        return CourseForCustomerDTO.builder()
                .courseId(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .priority(null)
                .categoryCourse(lsCategory)
                .urlImage(course.getUrlImage())
                .chapters(course.getChapters())
                .duration(course.getDuration())
                .status(customerCourse.getStatus().equals(CustomerCourse.CustomerCourseStatus.PROGRESS)?
                        CourseForCustomerDTO.CoursesForCustomerStatusDTO.PROGRESS: CourseForCustomerDTO.CoursesForCustomerStatusDTO.COMPLETED)
                .build();
    }
}
