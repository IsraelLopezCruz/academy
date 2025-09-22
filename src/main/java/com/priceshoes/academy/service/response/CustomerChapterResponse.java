package com.priceshoes.academy.service.response;

import com.priceshoes.academy.domain.CustomerCourse;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@Builder
public class CustomerChapterResponse {
    Long id;
    Long customerCourseId;
    Long chapterId;
    CustomerCourse.CustomerCourseStatus status;
}
