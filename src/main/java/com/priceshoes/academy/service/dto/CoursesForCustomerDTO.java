package com.priceshoes.academy.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@AllArgsConstructor
@Value
public class CoursesForCustomerDTO implements Serializable {
    private static final long serialVersionUID = 243593120738622128L;

    @NonNull
    private String customerId;
    @NonNull
    private CategoryDTO category;
    @Nullable
    private List<CourseForCustomerDTO> coursesForCustomer;

}
