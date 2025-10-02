package com.priceshoes.academy.converter;

import com.priceshoes.academy.domain.Course;
import com.priceshoes.academy.domain.CustomerCourse;
import com.priceshoes.academy.service.response.CustomerCourseResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;
@Component
@AllArgsConstructor
public class CustomerCourse2CustomerCourseResponseDTO implements Function<CustomerCourse, CustomerCourseResponse> {

       @Override
       public CustomerCourseResponse apply(CustomerCourse customerCourse) {
              return CustomerCourseResponse.builder()
                      .id(customerCourse.getId())
                      .courseId(customerCourse.getCourse().getId())
                      .customerId(customerCourse.getCustomerId())
                      .status(customerCourse.getStatus())
                      .build();
       }
}
