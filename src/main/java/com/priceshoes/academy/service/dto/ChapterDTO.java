package com.priceshoes.academy.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Value
@Builder
@AllArgsConstructor
public class ChapterDTO implements Serializable {
    private static final long serialVersionUID = -7255747069140938007L;

    private Long chapterId;
    private String title;
    private String description;
    private String urlMedia;
    private String titleResource;
    private String urlResource;
    private String duration;
    private boolean mainChapter;
    private ChapterStatusDTO status;

    public enum ChapterStatusDTO {
        NEW, PROGRESS, FINISH;
    }
}
