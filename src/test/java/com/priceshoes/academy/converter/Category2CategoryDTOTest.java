package com.priceshoes.academy.converter;

import com.priceshoes.academy.domain.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Category2CategoryDTOTest {

    private final Category2CategoryDTO subject = new Category2CategoryDTO();

    @Test
    void apply() {
        var category = Category.builder()
                .id(1L)
                .title("Titile")
                .priority(2)
                .build();

        var actual = subject.apply(category);

        assertEquals(actual.getCategoryId(), category.getId());
    }
}