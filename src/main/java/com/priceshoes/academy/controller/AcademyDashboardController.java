package com.priceshoes.academy.controller;


import com.priceshoes.academy.component.Stopwatch;
import com.priceshoes.academy.service.AcademyService;
import com.priceshoes.academy.service.dto.*;
import datadog.trace.api.Trace;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Academy admin controller")
@Slf4j
@RequestMapping("dashboard/")
@RestController
@RequiredArgsConstructor
public class AcademyDashboardController {
    @NonNull
    private AcademyService academyService;

    @Operation(
            summary = "Get categories",
            security = {@SecurityRequirement(name = "priceshoes_auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the created category"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping(value = "/categories")
    @Trace
    public ResponseEntity<List<CategoryDTO>> getCategoriesCourse() {
        return ResponseEntity.ok(academyService.getCategoriesCourse());
    }

    @Operation(
            summary = "Creates a category",
            security = {@SecurityRequirement(name = "priceshoes_auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the created category"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Stopwatch
    @PostMapping(value = "/category")
    @Trace
    public ResponseEntity<Void> createCategoryCourse(@NonNull @RequestBody CategoryCourseRequest categoryCourseRequest) {
        academyService.createCategoryCourse(categoryCourseRequest);
        return ResponseEntity.accepted().build();
    }

    @Operation(
            summary = "Creates a category",
            security = {@SecurityRequirement(name = "priceshoes_auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the created category"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Stopwatch
    @PatchMapping(value = "/category")
    @Trace
    public ResponseEntity<Void> updateCategoryCourse(@NonNull @RequestBody CategoryCourseRequest categoryCourseRequest) {
        academyService.updateCategoryCourse(categoryCourseRequest);
        return ResponseEntity.accepted().build();
    }

    @Operation(
            summary = "Get courses",
            security = {@SecurityRequirement(name = "priceshoes_auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the created category"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping(value = "/courses")
    @Trace
    public ResponseEntity<List<CourseDTO>> getCourses() {
        return ResponseEntity.ok(academyService.getCourses());
    }

    @Operation(
            summary = "Get chapters",
            security = {@SecurityRequirement(name = "priceshoes_auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the created category"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping(value = "/chapters/course/{courseId}")
    @Trace
    public ResponseEntity<List<ChapterDTO>> getChaptersFromCourse(@NonNull @PathVariable Long courseId) {
        return ResponseEntity.ok(academyService.getChaptersFromCourse(courseId));
    }

    @Operation(
            summary = "Create a course",
            security = {@SecurityRequirement(name = "priceshoes_auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the created course"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Stopwatch
    @PostMapping(value = "/course")
    @Trace
    public ResponseEntity<Void> createCourse(@NonNull @RequestBody CourseRequest courseRequest) {
        academyService.createCourse(courseRequest);
        return ResponseEntity.accepted().build();
    }

    @Operation(
            summary = "Get courses from category",
            security = {@SecurityRequirement(name = "priceshoes_auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the created category"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping(value = "/courses/category/{category}")
    @Trace
    public ResponseEntity<List<CourseDTO>> getCoursesFromCategory(@NonNull @PathVariable Long category) {
        return ResponseEntity.ok(academyService.getCoursesFromCategory(category));
    }

    @Operation(
            summary = "Create a course",
            security = {@SecurityRequirement(name = "priceshoes_auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the created course"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Stopwatch
    @PostMapping(value = "/chapter/course")
    @Trace
    public ResponseEntity<Void> addChapterCourse(@NonNull @RequestBody CourseChapterRequest courseChapterRequest) {
        academyService.addChapterCourse(courseChapterRequest);
        return ResponseEntity.accepted().build();
    }

    @Operation(
            summary = "Update a course",
            security = {@SecurityRequirement(name = "priceshoes_auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the created course"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Stopwatch
    @PatchMapping(value = "/course/information")
    @Trace
    public ResponseEntity<Void> updateCourseInformation(@NonNull @RequestBody CourseRequest courseRequest) {
        academyService.updateCourseInformation(courseRequest);
        return ResponseEntity.accepted().build();
    }

    @Operation(
            summary = "Delete a course",
            security = {@SecurityRequirement(name = "priceshoes_auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the created course"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Stopwatch
    @DeleteMapping(value = "/course")
    @Trace
    public ResponseEntity<Void> deleteCourse(@NonNull @RequestBody CourseDeleteRequest courseDeleteRequest) {
        academyService.deleteCourse(courseDeleteRequest);
        return ResponseEntity.accepted().build();
    }

    @Operation(
            summary = "Update a chapter",
            security = {@SecurityRequirement(name = "priceshoes_auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the created course"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Stopwatch
    @PatchMapping(value = "/chapters")
    @Trace
    public ResponseEntity<Void> updateCourseChapter(@NonNull @RequestBody ChapterRequest chapterRequest) {
        academyService.updateChapter(chapterRequest);
        return ResponseEntity.accepted().build();
    }

    @Operation(
            summary = "Delete chapters",
            security = {@SecurityRequirement(name = "priceshoes_auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the created course"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Stopwatch
    @DeleteMapping(value = "/chapters")
    @Trace
    public ResponseEntity<Void> deleteCourseChapters(@NonNull @RequestBody CourseDeleteRequest courseDeleteRequest) {
        academyService.deleteCourseChapters(courseDeleteRequest);
        return ResponseEntity.accepted().build();
    }

}
