package com.priceshoes.academy.service.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString(callSuper=true)
public class CourseDescriptionDTO {
    Long id;
    String description;

    public CourseDescriptionDTO(Long id, @NonNull String description) {
        this.id = id;
        this.description = description;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription( String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CourseDescriptionDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
