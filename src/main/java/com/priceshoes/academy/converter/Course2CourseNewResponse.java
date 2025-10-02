package com.priceshoes.academy.converter;

import com.priceshoes.academy.domain.Course;
import com.priceshoes.academy.service.response.CourseNewResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;
@Component
@AllArgsConstructor
public class Course2CourseNewResponse implements Function<Course, CourseNewResponse> {
    @Override
    public CourseNewResponse apply(Course course) {


       return CourseNewResponse.builder()
              .id(course.getId())
               .title(course.getTitle())
               .description(course.getDescription())
               .urlImage(course.getUrlImage())
               .chapters(course.getChapters())
               .duration(course.getDuration())
               .status(course.getStatus())
               .build();
   }
}
