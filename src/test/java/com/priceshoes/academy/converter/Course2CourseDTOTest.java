package com.priceshoes.academy.converter;

import com.priceshoes.academy.domain.Category;
import com.priceshoes.academy.domain.Chapter;
import com.priceshoes.academy.domain.Course;
import com.priceshoes.academy.domain.CourseCategory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Course2CourseDTOTest {

    private final Course2CourseDTO subject = new Course2CourseDTO();

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
        var courseCategory = CourseCategory.builder()
                .id(1L)
                .course(course)
                .category(Category.builder().id(1L).title("title").priority(1).build())
                .priority(1)
                .build();
        course.setCourseCategory(List.of(courseCategory));
        var chapter = Chapter.builder()
                .id(2L)
                .title("Title")
                .description("Description")
                .urlMedia("url")
                .duration("12 min")
                .course(course)
                .build();
        course.setChapter(List.of(chapter));

        var actual = subject.apply(course);

        assertEquals(actual.getId(), course.getId());
    }
}