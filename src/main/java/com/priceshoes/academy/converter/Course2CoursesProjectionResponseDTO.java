package com.priceshoes.academy.converter;


import com.priceshoes.academy.domain.Course;
import com.priceshoes.academy.service.response.CoursesProjectionResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class Course2CoursesProjectionResponseDTO implements Function<Course, CoursesProjectionResponse> {
    @Override
    public CoursesProjectionResponse apply(Course course) {
        List<CoursesProjectionResponse> listCoursesProjection = new ArrayList<>();
        CoursesProjectionResponse dto = new CoursesProjectionResponse();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        listCoursesProjection.add(dto);

        return dto;
    }
}
