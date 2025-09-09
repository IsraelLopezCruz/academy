package com.priceshoes.academy.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Value
@AllArgsConstructor
@Builder
public class CustomrChapterRequest {

    @NonNull
    private String customerId;
    @NonNull
    private Long courseId;
    @NonNull
    private Long chapterId;
    @NonNull
    private CustomrChapterStatusRequest status;

    public enum CustomrChapterStatusRequest {
        PROGRESS, FINISH;
    }
}
