package com.priceshoes.academy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.priceshoes.academy.component.MDCFilter;
import com.priceshoes.academy.configuration.Oauth2AuthenticationConfig;
import com.priceshoes.academy.service.AcademyService;
import com.priceshoes.academy.service.dto.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AcademyController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {MDCFilter.class})}
)
@ImportAutoConfiguration(classes = {Oauth2AuthenticationConfig.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class AcademyControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private AcademyService academyService;

    @Test
    @WithMockUser(roles = "admin")
    void getCoursesForCustomer() throws Exception {
        var customerId = "04637536374";
        var response = List.of(CoursesForCustomerDTO.builder()
                .customerId(customerId)
                .category(CategoryDTO.builder().build())
                .coursesForCustomer(List.of(CourseForCustomerDTO.builder().courseId(1L).build()))
                .build());
        when(academyService.getCoursesForCustomer(customerId)).thenReturn(response);

        var actual = mockMvc.perform(get("/courses/customer/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt().jwt(c -> c.claim("username", customerId)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var actualResponse = objectMapper.readValue(actual, CoursesForCustomerDTO[].class);

        assertEquals(response.getFirst().getCustomerId(), Arrays.stream(actualResponse).findFirst().get().getCustomerId());
        verify(academyService).getCoursesForCustomer(customerId);
        verifyNoMoreInteractions(academyService);
    }

    @Test
    @WithMockUser(roles = "admin")
    void getCourseForCustomer() throws Exception {
        var customerId = "04637536374";
        var courseId = 1L;
        var categoryId = 1L;
        var response = CourseForCustomerDTO.builder()
                .courseId(courseId)
                .customerChapters(List.of(ChapterDTO.builder().chapterId(1L).build()))
                .build();
        when(academyService.getCourseForCustomer(courseId, categoryId, customerId)).thenReturn(response);

        var actual = mockMvc.perform(get("/course/" +courseId + "/"+ categoryId + "/"+ customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt().jwt(c -> c.claim("username", customerId)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var actualResponse = objectMapper.readValue(actual, CourseForCustomerDTO.class);

        assertEquals(response, actualResponse);
        verify(academyService).getCourseForCustomer(courseId, categoryId, customerId);
        verifyNoMoreInteractions(academyService);
    }

    @Test
    @WithMockUser(roles = "admin")
    void createCustomerCourseChapter() throws Exception {
        var request = CustomrChapterRequest.builder()
                .customerId("11200004849")
                .courseId(1L)
                .chapterId(1L)
                .status(CustomrChapterRequest.CustomrChapterStatusRequest.PROGRESS)
                .build();
        doNothing().when(academyService).createCustomerCourseChapter(request);

        mockMvc.perform(post("/customer/chapter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isAccepted())
                .andReturn();

        verify(academyService).createCustomerCourseChapter(request);
        verifyNoMoreInteractions(academyService);
    }

}