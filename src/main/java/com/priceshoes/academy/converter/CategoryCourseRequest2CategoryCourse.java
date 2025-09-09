package com.priceshoes.academy.converter;

import com.priceshoes.academy.domain.Category;
import com.priceshoes.academy.service.dto.CategoryCourseRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.function.Function;


@Component
@AllArgsConstructor
public class CategoryCourseRequest2CategoryCourse implements Function<CategoryCourseRequest, Category> {

    @Override
    public Category apply(@NonNull CategoryCourseRequest categoryCourseRequest) {
        return Category.builder()
                .title(categoryCourseRequest.getTitle())
                .priority(categoryCourseRequest.getPriority())
                .build();
    }
}
