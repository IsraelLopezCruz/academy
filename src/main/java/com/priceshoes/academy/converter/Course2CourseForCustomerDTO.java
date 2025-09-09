package com.priceshoes.academy.converter;

import com.priceshoes.academy.domain.Course;
import com.priceshoes.academy.domain.CourseCategory;
import com.priceshoes.academy.domain.CustomerCourse;
import com.priceshoes.academy.domain.CustomerCourseChapter;
import com.priceshoes.academy.service.dto.CategoryDTO;
import com.priceshoes.academy.service.dto.ChapterDTO;
import com.priceshoes.academy.service.dto.CourseForCustomerDTO;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class Course2CourseForCustomerDTO implements BiFunction<CourseCategory, Optional<CustomerCourse>, CourseForCustomerDTO> {


    @Override
    public CourseForCustomerDTO apply(@NonNull CourseCategory courseCategory,@NonNull Optional<CustomerCourse> optCustomerCourse) {
        var course = courseCategory.getCourse();
        var category = courseCategory.getCategory();
        List<CustomerCourseChapter> lstCustomerCourseChapter = optCustomerCourse.isPresent()? optCustomerCourse.get().getCustomerCourseChapters(): List.of();
        Map<Long, CustomerCourseChapter> mapChapterIds = lstCustomerCourseChapter.isEmpty()? new HashMap<Long, CustomerCourseChapter>()
                : lstCustomerCourseChapter.stream().collect(Collectors.toMap(c -> c.getChapter().getId(), Function.identity()));

        var lstCourseChapters = course.getChapter();
        var lstCourseChaptersDTO = lstCourseChapters.stream().map(cc -> ChapterDTO.builder()
                .chapterId(cc.getId())
                .title(cc.getTitle())
                .description(cc.getDescription())
                .urlMedia(cc.getUrlMedia())
                .titleResource(cc.getTitleResource())
                .urlResource(cc.getUrlResource())
                .duration(cc.getDuration())
                .mainChapter(cc.isMainChapter())
                .status(mapChapterIds.containsKey(cc.getId())? ChapterDTO.ChapterStatusDTO.valueOf(mapChapterIds.get(cc.getId()).getStatus().name()) : ChapterDTO.ChapterStatusDTO.NEW)
                .build()).toList();

        var status = optCustomerCourse.map(customerCourse ->  {
            if(customerCourse.getStatus().equals(CustomerCourse.CustomerCourseStatus.FINISH)){
                return CourseForCustomerDTO.CoursesForCustomerStatusDTO.COMPLETED;
            }else {
                return CourseForCustomerDTO.CoursesForCustomerStatusDTO.valueOf(customerCourse.getStatus().name());
            }
        } ).orElse(CourseForCustomerDTO.CoursesForCustomerStatusDTO.NEW);

        return CourseForCustomerDTO.builder()
                .courseId(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .priority(courseCategory.getPriority())
                .categoryCourse(List.of(CategoryDTO.builder()
                                .title(category.getTitle())
                                .categoryId(category.getId())
                                .priority(category.getPriority()).build()))
                .urlImage(course.getUrlImage())
                .urlResource(optCustomerCourse.map(CustomerCourse::getUrlResource).orElse(null))
                .chapters(course.getChapters())
                .duration(course.getDuration())
                .status(status)
                .customerChapters(lstCourseChaptersDTO)
                .build();
    }
}
