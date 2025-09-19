package com.priceshoes.academy.controller.request;

import com.priceshoes.academy.domain.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString(callSuper = true)
public class CourseStatusRequest {
    Long id;
    String title;
    Course.CourseStatus status;
}
