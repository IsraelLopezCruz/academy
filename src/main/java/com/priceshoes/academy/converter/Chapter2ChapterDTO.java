package com.priceshoes.academy.converter;

import com.priceshoes.academy.domain.Chapter;
import com.priceshoes.academy.service.dto.ChapterDTO;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@AllArgsConstructor
public class Chapter2ChapterDTO implements Function<Chapter, ChapterDTO> {

    @Override
    public ChapterDTO apply(@NonNull Chapter chapter) {
        return  ChapterDTO.builder()
                .chapterId(chapter.getId())
                .title(chapter.getTitle())
                .description(chapter.getDescription())
                .urlMedia(chapter.getUrlMedia())
                .titleResource(chapter.getTitleResource())
                .urlResource(chapter.getUrlResource())
                .duration(chapter.getDuration())
                .mainChapter(chapter.isMainChapter())
                .build();
    }
}
