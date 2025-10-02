package com.priceshoes.academy.service.response;

import com.priceshoes.academy.domain.Course;
import lombok.*;

@Getter
@Setter
@ToString(callSuper=true)
@AllArgsConstructor
@Builder
public class CourseNewResponse {
    Long id;
    String title;
    String description;
    String urlImage;
    Integer chapters;
    String duration;
    Course.CourseStatus status;
}
