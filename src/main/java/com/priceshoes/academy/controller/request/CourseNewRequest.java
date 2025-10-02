package com.priceshoes.academy.controller.request;

import com.priceshoes.academy.domain.Course;
import lombok.*;

@Getter
@Setter
@ToString(callSuper=true)
@AllArgsConstructor
@Builder

public class CourseNewRequest {
    Long id;
    String title;
    String description;
    String urlImage;
    Integer chapters;
    String duration;
    Course.CourseStatus status;
}
