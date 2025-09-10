package com.priceshoes.academy.controller;

import com.priceshoes.academy.service.AcademyService;
import com.priceshoes.academy.service.dto.*;
import com.priceshoes.academy.service.response.CustomerCompliedResponse;
import datadog.trace.api.Trace;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/list/{customerId}")
    public List<CustomerCompliedResponse> getListCourse(@PathVariable String  customerId){
        return academyService.getListCourses(customerId);
    }


}