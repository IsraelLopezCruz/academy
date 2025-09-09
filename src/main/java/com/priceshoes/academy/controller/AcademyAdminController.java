package com.priceshoes.academy.controller;


import com.priceshoes.academy.component.Stopwatch;
import com.priceshoes.academy.service.AcademyService;
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


@Tag(name = "Academy admin controller")
@Slf4j
@RequestMapping("adm/")
@RestController
@RequiredArgsConstructor
public class AcademyAdminController {
    @NonNull
    private AcademyService academyService;


    @Operation(
            summary = "delete cache",
            security = {@SecurityRequirement(name = "priceshoes_auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the created game"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Stopwatch
    @DeleteMapping(value = "/cache")
    @Trace
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Void> deleteCache() {
        academyService.deleteCache();
        return ResponseEntity.accepted().build();
    }
}
