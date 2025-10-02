package com.priceshoes.academy.converter;

import com.priceshoes.academy.domain.CustomerCourseChapter;
import com.priceshoes.academy.service.dto.CategoryDTO;
import com.priceshoes.academy.service.dto.ChapterStatusDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class CustomerCourseChapter2ChapterStatusDTO  implements Function<CustomerCourseChapter, ChapterStatusDTO> {

    @Override
    public ChapterStatusDTO apply(CustomerCourseChapter customerCourseChapter) {
        List<ChapterStatusDTO> chapterStatuses = new ArrayList<>();
        ChapterStatusDTO dto = new ChapterStatusDTO();
        dto.setId(customerCourseChapter.getChapter().getId());
        dto.setStatus(customerCourseChapter.getStatus().name());
        dto.setTitle(customerCourseChapter.getChapter().getTitle());
        dto.setCourseId(customerCourseChapter.getChapter().getCourse().getId());
        dto.setCustomerId(customerCourseChapter.getCustomerCourse().getCustomerId());
        chapterStatuses.add(dto);
        return dto;
    }
}
