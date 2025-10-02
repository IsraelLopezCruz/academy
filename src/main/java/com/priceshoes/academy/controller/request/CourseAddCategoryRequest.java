package com.priceshoes.academy.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourseAddCategoryRequest {
    Long courseId;
    Long categoryId;
    Integer priority;
}
