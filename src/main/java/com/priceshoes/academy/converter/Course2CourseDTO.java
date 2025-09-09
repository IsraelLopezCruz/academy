package com.priceshoes.academy.converter;

import com.priceshoes.academy.domain.Chapter;
import com.priceshoes.academy.domain.Course;
import com.priceshoes.academy.domain.CourseCategory;
import com.priceshoes.academy.service.dto.CategoryDTO;
import com.priceshoes.academy.service.dto.ChapterDTO;
import com.priceshoes.academy.service.dto.CourseDTO;
import com.priceshoes.academy.service.dto.CourseRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class Course2CourseDTO implements Function<Course, CourseDTO> {


    @Override
    public CourseDTO apply(@NonNull Course course) {
        var lstCourseChapters = course.getChapter();
        List<ChapterDTO> lstChapter = lstCourseChapters.stream().map(ch -> ChapterDTO.builder()
                .chapterId(ch.getId())
                .title(ch.getTitle())
                .description(ch.getDescription())
                .urlMedia(ch.getUrlMedia())
                .titleResource(ch.getTitleResource())
                .urlResource(ch.getUrlResource())
                .duration(ch.getDuration())
                .mainChapter(ch.isMainChapter())
                .build()).toList();

        var lstCategory = course.getCourseCategory().stream()
                .map(cc -> CategoryDTO.builder()
                        .categoryId(cc.getCategoryId())
                        .title(cc.getCategory().getTitle())
                        .priority(cc.getPriority())
                        .build()).toList();

        return CourseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .category(lstCategory)
                .urlImage(course.getUrlImage())
                .urlResource(course.getUrlResource())
                .chapters(course.getChapters())
                .duration(course.getDuration())
                .status(CourseDTO.CourseDTOStatus.valueOf(course.getStatus().name()))
                .chapter(lstChapter)
                .build();
    }
}
