package com.priceshoes.academy.converter;

import com.priceshoes.academy.domain.Chapter;
import com.priceshoes.academy.domain.Course;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Chapter2ChapterDTOTest {

    private final Chapter2ChapterDTO subject = new Chapter2ChapterDTO();

    @Test
    void apply() {
        var course = Course.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .urlImage("url")
                .chapters(1)
                .urlResource("url")
                .status(Course.CourseStatus.AVAILABLE)
                .build();
        var chapter = Chapter.builder()
                .id(2L)
                .title("Title")
                .description("Description")
                .urlMedia("url")
                .duration("12 min")
                .course(course)
                .build();

        var actual = subject.apply(chapter);

        assertEquals(actual.getChapterId(), chapter.getId());
    }
}