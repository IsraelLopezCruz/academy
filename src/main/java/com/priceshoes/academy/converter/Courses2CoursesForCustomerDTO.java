package com.priceshoes.academy.converter;

import com.priceshoes.academy.domain.Course;
import com.priceshoes.academy.domain.CustomerCourse;
import com.priceshoes.academy.service.dto.CategoryDTO;
import com.priceshoes.academy.service.dto.CourseForCustomerDTO;
import com.priceshoes.academy.service.dto.CoursesForCustomerDTO;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.function.TriFunction;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;

@Component
@AllArgsConstructor
public class Courses2CoursesForCustomerDTO implements TriFunction<List<Course>, List<CustomerCourse>, String, List<CoursesForCustomerDTO>> {

    private CustomerCourse2CourseForCustomerDTO customerCourse2CourseForCustomerDTO;

    @Override
    public List<CoursesForCustomerDTO> apply(@NonNull List<Course> courses,@NonNull List<CustomerCourse> customerCourses, @NonNull String customerId) {
        List<CourseForCustomerDTO> listTotalCourses = new ArrayList<>(100);
        Set<CategoryDTO> setCategories = new HashSet<>();

        customerCourses.forEach(cc -> listTotalCourses.addAll(getCourseForCustomerDTOFromCustomerCourse(cc)));
        courses.forEach(c -> listTotalCourses.addAll(getCourseForCustomerDTOFromCourse(c)));

        listTotalCourses.forEach(ct -> setCategories.addAll(ct.getCategoryCourse()));
        var listCategory = setCategories.stream().sorted(Comparator.comparing(CategoryDTO::getPriority)).toList();

        Comparator<CourseForCustomerDTO> comparatorSortedCourse = Comparator.comparing(CourseForCustomerDTO::getPriority).thenComparing(c -> {
            if(c.getStatus().equals(CourseForCustomerDTO.CoursesForCustomerStatusDTO.COMPLETED)){
                return 1;
            }else {
                return 0;
            }
        });

        return listCategory.stream()
                .map(cat -> CoursesForCustomerDTO.builder()
                .customerId(customerId)
                .category(cat)
                .coursesForCustomer(listTotalCourses.stream()
                        .filter(ltc -> ltc.getCategoryCourse().contains(cat))
                        .sorted(comparatorSortedCourse).toList()
                )
                .build()).toList();
    }

    private List<CourseForCustomerDTO> getCourseForCustomerDTOFromCustomerCourse(CustomerCourse customerCourse){
        var course = customerCourse.getCourse();
        var lsCourseCategory = course.getCourseCategory();

        return lsCourseCategory.stream().map( cat -> CourseForCustomerDTO.builder()
                .courseId(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .priority(cat.getPriority())
                .categoryCourse(List.of(CategoryDTO.builder()
                    .categoryId(cat.getCategoryId())
                    .title(cat.getCategory().getTitle())
                    .priority(cat.getCategory().getPriority()).build())
                )
                .urlImage(course.getUrlImage())
                .chapters(course.getChapters())
                .duration(course.getDuration())
                .status(customerCourse.getStatus().equals(CustomerCourse.CustomerCourseStatus.PROGRESS)?
                        CourseForCustomerDTO.CoursesForCustomerStatusDTO.PROGRESS: CourseForCustomerDTO.CoursesForCustomerStatusDTO.COMPLETED)
                .build()).toList();
    }


    private List<CourseForCustomerDTO> getCourseForCustomerDTOFromCourse(Course course){
        var lsCourseCategory = course.getCourseCategory();

        return lsCourseCategory.stream().map(cat -> CourseForCustomerDTO.builder()
                .courseId(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .categoryCourse(List.of(CategoryDTO.builder()
                        .categoryId(cat.getCategoryId())
                        .title(cat.getCategory().getTitle())
                        .priority(cat.getCategory().getPriority()).build())
                )
                .urlImage(course.getUrlImage())
                .chapters(course.getChapters())
                .priority(cat.getPriority())
                .duration(course.getDuration())
                .status(course.getStatus().equals(Course.CourseStatus.SOON)? CourseForCustomerDTO.CoursesForCustomerStatusDTO.SOON : CourseForCustomerDTO.CoursesForCustomerStatusDTO.NEW)
                .build()).toList();
    }
}
