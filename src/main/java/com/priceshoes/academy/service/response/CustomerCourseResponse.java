package com.priceshoes.academy.service.response;

import com.priceshoes.academy.domain.Course;
import com.priceshoes.academy.domain.CustomerCourse;
import lombok.*;

@Getter
@Setter
@ToString(callSuper=true)
@Builder
@AllArgsConstructor
public class CustomerCourseResponse {
    Long id;
    String customerId;
    Long courseId;
    CustomerCourse.CustomerCourseStatus status;
}
