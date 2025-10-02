package com.priceshoes.academy.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString(callSuper=true)
public class CourseResponse {
    Long id;
    String title;
    String description;
}
