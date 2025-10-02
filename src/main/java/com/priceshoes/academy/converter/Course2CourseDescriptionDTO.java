package com.priceshoes.academy.converter;

import com.priceshoes.academy.domain.Course;
import com.priceshoes.academy.service.dto.CourseDescriptionDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@AllArgsConstructor
public class Course2CourseDescriptionDTO implements Function<Course, CourseDescriptionDTO> {
    @Override
    public CourseDescriptionDTO apply(Course course) {
        return null;
    }
}
