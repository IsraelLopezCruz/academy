package com.priceshoes.academy.converter;

import com.priceshoes.academy.domain.Category;
import com.priceshoes.academy.domain.Chapter;
import com.priceshoes.academy.domain.Course;
import com.priceshoes.academy.domain.CourseCategory;
import com.priceshoes.academy.service.dto.CourseRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CourseRequest2Course implements Function<CourseRequest, Course> {

    @Override
    public Course apply(@NonNull CourseRequest courseRequest) {

        var course =  Course.builder()
                .id(courseRequest.getId())
                .title(courseRequest.getTitle())
                .description(courseRequest.getDescription())
                .urlImage(courseRequest.getUrlImage())
                .urlResource(courseRequest.getUrlResource())
                .chapters(0)
                .duration(courseRequest.getDuration())
                .status(Course.CourseStatus.valueOf(courseRequest.getStatus().name()))
                .build();

        if(Objects.nonNull(courseRequest.getCourseCategory())){
            List<CourseCategory> courseCategory = courseRequest.getCourseCategory().stream()
                    .map(c -> CourseCategory.builder().course(course)
                            .categoryId(c.getCategoryId())
                            .priority(c.getPriority())
                            .build())
                    .collect(Collectors.toList());

            course.setCourseCategory(courseCategory);
        }

        if(Objects.nonNull(courseRequest.getChapter())){
            var lstCourseChapters = courseRequest.getChapter();
            List<Chapter> lstChapter = lstCourseChapters.stream().map(ch -> Chapter.builder()
                    .id(ch.getId())
                    .title(ch.getTitle())
                    .description(ch.getDescription())
                    .urlMedia(ch.getUrlMedia())
                    .titleResource(ch.getTitleResource())
                    .urlResource(ch.getUrlResource())
                    .duration(ch.getDuration())
                    .course(course)
                    .mainChapter(ch.isMainChapter())
                    .build()).collect(Collectors.toUnmodifiableList());

            course.setChapter(lstChapter);
        }
        return course;
    }
}
