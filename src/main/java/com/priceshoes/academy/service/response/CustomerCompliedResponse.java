package com.priceshoes.academy.service.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.priceshoes.academy.domain.Course;

import java.time.LocalDateTime;

public class CustomerCompliedResponse {
    Long id;
    String title;
    String description;
    Course.CourseStatus status;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDateTime createdAt;

    public CustomerCompliedResponse(Long id, String title, String description, Course.CourseStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Course.CourseStatus getStatus() {
        return status;
    }
    public void setStatus(Course.CourseStatus status) {
        this.status = status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AcademyServiceResponse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
