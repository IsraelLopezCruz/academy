package com.priceshoes.academy.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Value
@Builder
@AllArgsConstructor
public class CourseDTO implements Serializable {
    private static final long serialVersionUID = -3534803637599523933L;

    private Long id;
    private String title;
    private String description;
    private List<CategoryDTO> category;
    private String urlImage;
    private String urlResource;
    private Integer chapters;
    private String duration;
    private CourseDTOStatus status;
    private List<ChapterDTO> chapter;

    public enum CourseDTOStatus {
        SOON, AVAILABLE, EXPIRED;
    }
}
