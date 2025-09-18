package com.priceshoes.academy.service.response;

import com.priceshoes.academy.domain.Course;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString(callSuper=true)
public class CourseStatusDTO {
    Long id;
    String title;
    Course.CourseStatus status;
}
