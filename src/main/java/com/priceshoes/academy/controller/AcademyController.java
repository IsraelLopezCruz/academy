package com.priceshoes.academy.controller;

import com.priceshoes.academy.controller.request.CourseDescriptionRequest;
import com.priceshoes.academy.controller.request.CourseStatusRequest;
import com.priceshoes.academy.domain.Course;
import com.priceshoes.academy.service.AcademyService;
import com.priceshoes.academy.service.dto.*;
import com.priceshoes.academy.service.response.CourseStatusDTO;
import com.priceshoes.academy.service.response.CoursesProjectionResponse;
import datadog.trace.api.Trace;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SecurityScheme(name = "priceshoes_auth", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(clientCredentials = @OAuthFlow(tokenUrl = "${springdoc.auth_url}")))
@RestController
@AllArgsConstructor
@Slf4j
public class AcademyController {
    private final AcademyService academyService;


    @GetMapping(value = "/courses/customer/{customerId}")
    @PreAuthorize("(#customerId != null and #customerId == principal.getClaimAsString(\"username\")) or hasRole('admin')")
    @Trace
    public ResponseEntity<List<CoursesForCustomerDTO>> getCoursesForCustomer(@NonNull @PathVariable String customerId) {
        log.debug("Getting courses for customerId '{}'", customerId);
        return ResponseEntity.ok(academyService.getCoursesForCustomer(customerId));
    }

    @GetMapping(value = "/course/{courseId}/{categoryId}/{customerId}")
    @PreAuthorize("(#customerId != null and #customerId == principal.getClaimAsString(\"username\")) or hasRole('admin')")
    @Trace
    public ResponseEntity<CourseForCustomerDTO> getCourseForCustomer(@NonNull @PathVariable Long courseId, @NonNull @PathVariable Long categoryId, @NonNull @PathVariable String customerId) {
        log.debug("Getting course for customer '{}' '{}'", courseId, customerId);
        return ResponseEntity.ok(academyService.getCourseForCustomer(courseId, categoryId, customerId));
    }

    @PostMapping(value = "/customer/chapter")
    @PreAuthorize("(#customrChapterRequest.customerId != null and #customrChapterRequest.customerId == principal.getClaimAsString(\"username\")) or hasRole('admin')")
    @Trace
    public ResponseEntity<Void> createCustomerCourseChapter(@RequestBody @NonNull CustomrChapterRequest customrChapterRequest) {
        log.debug("Creating customer chapter '{}' for customer '{}'", customrChapterRequest.getChapterId(), customrChapterRequest.getCustomerId());
        academyService.createCustomerCourseChapter(customrChapterRequest);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/total/customer")
    public long getTotalCurtomer(){
        return academyService.getTotalSocias();
    }
    @GetMapping("/total/{courseId}")
    public long getTotalCurtomer(@NonNull @PathVariable Long courseId){
        return academyService.getTotalCourses(courseId);
    }

    @GetMapping("/chapter/finished/{courseId}/{customerId}")
    public List<ChapterStatusDTO> getChapterFinishedStatus(@NonNull @PathVariable Long courseId, @NonNull @PathVariable String customerId) {
        return academyService.getChapterStatuses(courseId,customerId);
    }
    @GetMapping("/customer/course/{customerId}")
    public List<CoursesProjectionResponse> getCoursesProjection(@NonNull @PathVariable String customerId) {
        return academyService.getCoursesProjection(customerId);
    }
    @GetMapping("course/never/completed")
    public List<CoursesProjectionResponse>  getCoursesNotCompleted() {
        return academyService.getCoursesNotCompleted();
    }
    @GetMapping("/course/completed/{customerId}")
    public boolean getCourseCompleted(@NonNull @PathVariable String customerId) {
        return academyService.getHasSeenAllCurses(customerId);
    }
    @PatchMapping("/update/description/course")
    public ResponseEntity<CourseDescriptionDTO> updateCourseDescription(@NonNull @RequestBody CourseDescriptionRequest courseDescriptionRequest) {
        return ResponseEntity.of(Optional.ofNullable(academyService.updateCourseDescription(courseDescriptionRequest)));
    }
    @PatchMapping("/update/disable/course")
    public ResponseEntity<Void> updateCourseStatus(@RequestBody CourseStatusRequest courseStatusRequest) {
        academyService.updateCourseStatus(courseStatusRequest);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/enable/course")
    public ResponseEntity<Course> updateCourseEnable(Course.CourseStatus status) {
        academyService.updateEnableAllCourses(status);
        return ResponseEntity.ok().build();
    }
}