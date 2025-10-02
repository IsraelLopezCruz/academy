package com.priceshoes.academy.converter;

import com.priceshoes.academy.controller.request.CourseNewRequest;
import com.priceshoes.academy.domain.Course;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;
@Component
@AllArgsConstructor
public class CourseNewRequetDTO2Course implements Function<CourseNewRequest, Course> {
    @Override
    public Course apply(CourseNewRequest courseNewRequest) {

        return Course.builder()
                .title(courseNewRequest.getTitle())
                .description(courseNewRequest.getDescription())
                .urlImage(courseNewRequest.getUrlImage())
                .chapters(courseNewRequest.getChapters())
                .duration(courseNewRequest.getDuration())
                .status(courseNewRequest.getStatus())
                .build();
    }
}
