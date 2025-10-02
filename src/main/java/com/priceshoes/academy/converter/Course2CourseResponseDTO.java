package com.priceshoes.academy.converter;

import com.priceshoes.academy.domain.Course;
import com.priceshoes.academy.service.response.CourseResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
@Component
@AllArgsConstructor
public class Course2CourseResponseDTO implements Function<Course, CourseResponse> {

    @Override
    public CourseResponse apply(Course course) {
        List<CourseResponse> listCoursesPro = new ArrayList<>();
        CourseResponse dto = new CourseResponse();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        listCoursesPro.add(dto);
        return dto;
    }
}
