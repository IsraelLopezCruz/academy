package com.priceshoes.academy.controller.request;

import com.priceshoes.academy.domain.CustomerCourse;
import com.priceshoes.academy.domain.CustomerCourseChapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
@AllArgsConstructor
public class CustomerChapterRequest {
    Long customerCourseId;
    Long chapterId;
    CustomerCourseChapter.CustomerCourseChapterStatus status;
}
