package com.priceshoes.academy.controller.request;

import com.priceshoes.academy.domain.Course;
import com.priceshoes.academy.domain.CustomerCourse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
@AllArgsConstructor
public class CustomerCourseRequest {
    String customerId;
    Long courseId;
    CustomerCourse.CustomerCourseStatus status;
}
