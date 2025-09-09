package com.priceshoes.academy.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.List;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Value
@Builder
@AllArgsConstructor
public class CourseForCustomerDTO implements Serializable {
    private static final long serialVersionUID = -2920242956552137395L;

    private Long courseId;
    private String title;
    private String description;
    private List<CategoryDTO> categoryCourse;
    private Integer priority;
    private String urlImage;
    private String urlResource;
    private Integer chapters;
    private String duration;
    private CoursesForCustomerStatusDTO status;
    private List<ChapterDTO> customerChapters;

    public enum CoursesForCustomerStatusDTO {
        SOON, NEW, PROGRESS, COMPLETED;
    }
}
