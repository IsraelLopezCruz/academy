package com.priceshoes.academy.converter;

import com.priceshoes.academy.domain.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerCourse2CourseForCustomerDTOTest {

    private final CustomerCourse2CourseForCustomerDTO subject = new CustomerCourse2CourseForCustomerDTO();

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
        var customerCourse = CustomerCourse.builder()
                .id(1L)
                .customerId("11200004849")
                .status(CustomerCourse.CustomerCourseStatus.PROGRESS)
                .urlResource("urlResource")
                .course(course)
                .build();
        List<CustomerCourseChapter> customerCourseChapter = List.of(CustomerCourseChapter.builder()
                .id(2L)
                .chapter(chapter)
                .status(CustomerCourseChapter.CustomerCourseChapterStatus.PROGRESS)
                .customerCourse(customerCourse).build());
        customerCourse.setCustomerCourseChapters(customerCourseChapter);

        var actual = subject.apply(customerCourse);

        assertEquals(actual.getCourseId(), course.getId());
        assertEquals(actual.getTitle(), course.getTitle());
    }
}