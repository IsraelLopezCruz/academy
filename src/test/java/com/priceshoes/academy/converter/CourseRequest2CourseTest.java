package com.priceshoes.academy.converter;

import com.priceshoes.academy.service.dto.ChapterRequest;
import com.priceshoes.academy.service.dto.CourseRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseRequest2CourseTest {

    private final CourseRequest2Course subject = new CourseRequest2Course();

    @Test
    void apply() {
        var request = CourseRequest.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .urlImage("urlImage")
                .duration("12min")
                .status(CourseRequest.CourseRequesStatus.AVAILABLE)
                .chapter(List.of(ChapterRequest.builder()
                        .title("Title")
                        .description("Description")
                        .urlMedia("urlMedia")
                        .duration("12 min").build())).build();

        var actual = subject.apply(request);

        assertEquals(actual.getTitle(), request.getTitle());
    }
}