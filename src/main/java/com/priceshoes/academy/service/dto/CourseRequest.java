package com.priceshoes.academy.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.util.List;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Value
@Builder
@AllArgsConstructor
public class CourseRequest {

    @Nullable
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String description;
    private List<CourseCategoryRequest> courseCategory;
    @NonNull
    private String urlImage;
    private String urlResource;
    @NonNull
    private String duration;
    @NonNull
    private CourseRequesStatus status;
    @Nullable
    private List<ChapterRequest> chapter;

    public enum CourseRequesStatus {
        SOON, AVAILABLE, EXPIRED;
    }
}
