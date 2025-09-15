package com.priceshoes.academy.service.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString(callSuper = true)
public class ChapterStatusDTO {
        Long Id;
        Long courseId;
        String title;
        String Status;
        String customerId;
}
