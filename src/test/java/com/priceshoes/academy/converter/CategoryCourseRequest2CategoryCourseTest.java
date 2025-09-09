package com.priceshoes.academy.converter;

import com.priceshoes.academy.service.dto.CategoryCourseRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryCourseRequest2CategoryCourseTest {

    private final CategoryCourseRequest2CategoryCourse subject = new CategoryCourseRequest2CategoryCourse();

    @Test
    void apply() {
        var request = CategoryCourseRequest.builder()
                .id(1L)
                .title("Titile")
                .priority(1)
                .build();

        var actual = subject.apply(request);

        assertEquals(actual.getTitle(), request.getTitle());
    }
}