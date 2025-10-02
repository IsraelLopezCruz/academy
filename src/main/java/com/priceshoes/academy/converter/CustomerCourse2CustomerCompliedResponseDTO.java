package com.priceshoes.academy.converter;

import com.priceshoes.academy.domain.Course;
import com.priceshoes.academy.domain.CustomerCourse;
import com.priceshoes.academy.service.response.CustomerCompliedResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.function.Function;
@Component
@AllArgsConstructor
public class CustomerCourse2CustomerCompliedResponseDTO implements Function<CustomerCourse, CustomerCompliedResponse> {
    @Override
    public CustomerCompliedResponse apply(CustomerCourse customerCourse) {
        return new CustomerCompliedResponse(customerCourse.getCourse().getId(),
                customerCourse.getCourse().getTitle(),
                customerCourse.getCourse().getDescription(),
                customerCourse.getCourse().getStatus(),
                customerCourse.getCourse().getCreatedAt());
    }
}
