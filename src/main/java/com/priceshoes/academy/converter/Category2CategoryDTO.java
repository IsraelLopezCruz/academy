package com.priceshoes.academy.converter;

import com.priceshoes.academy.domain.Category;
import com.priceshoes.academy.service.dto.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@AllArgsConstructor
public class Category2CategoryDTO implements Function<Category, CategoryDTO> {

    @Override
    public CategoryDTO apply(@NonNull Category category) {
        return CategoryDTO.builder()
                .categoryId(category.getId())
                .title(category.getTitle())
                .priority(category.getPriority())
                .build();
    }
}
