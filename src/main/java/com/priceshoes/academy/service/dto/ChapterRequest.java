package com.priceshoes.academy.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Value
@Builder
@AllArgsConstructor
public class ChapterRequest {
    @Nullable
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private String urlMedia;
    private String titleResource;
    private String urlResource;
    @NonNull
    private String duration;
    private boolean mainChapter;
}
