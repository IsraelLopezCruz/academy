package com.priceshoes.academy.service.response;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString(callSuper=true)
public class CoursesProjectionResponse {
    Long id;
    String title;
    String description;
}
