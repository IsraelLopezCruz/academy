package com.priceshoes.academy.controller.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString(callSuper=true)
public class CourseDescriptionRequest {
    Long id;
    String description;
}
