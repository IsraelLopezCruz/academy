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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AcademyDashboardController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {MDCFilter.class})}
)
@ImportAutoConfiguration(classes = {Oauth2AuthenticationConfig.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class AcademyDashboardControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private AcademyService academyService;


    @Test
    @WithMockUser(roles = "admin")
    void getCategoriesCourse() throws Exception {
        var customerId = "04637536374";
        var response = List.of(CategoryDTO.builder().categoryId(1L).title("title").priority(1).build());
        when(academyService.getCategoriesCourse()).thenReturn(response);

        var actual = mockMvc.perform(get("/dashboard/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt().jwt(c -> c.claim("username", customerId)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var actualResponse = objectMapper.readValue(actual, CategoryDTO[].class);

        assertEquals(response.getFirst().getCategoryId(), Arrays.stream(actualResponse).findFirst().get().getCategoryId());
        verify(academyService).getCategoriesCourse();
        verifyNoMoreInteractions(academyService);
    }

    @Test
    @WithMockUser(roles = "admin")
    void createCategoryCourse() throws Exception {
        var request = CategoryCourseRequest.builder()
                .title("Titile")
                .priority(1)
                .build();
        doNothing().when(academyService).createCategoryCourse(request);

        mockMvc.perform(post("/dashboard/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isAccepted())
                .andReturn();

        verify(academyService).createCategoryCourse(request);
        verifyNoMoreInteractions(academyService);
    }

    @Test
    @WithMockUser(roles = "admin")
    void updateCategoryCourse() throws Exception {
        var request = CategoryCourseRequest.builder()
                .id(1L)
                .title("Titile")
                .priority(1)
                .build();
        doNothing().when(academyService).updateCategoryCourse(request);

        mockMvc.perform(patch("/dashboard/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isAccepted())
                .andReturn();

        verify(academyService).updateCategoryCourse(request);
        verifyNoMoreInteractions(academyService);
    }

    @Test
    @WithMockUser(roles = "admin")
    void getCourses() throws Exception {
        var customerId = "04637536374";
        var response = List.of(CourseDTO.builder()
                .id(1L).title("title")
                .category(List.of(CategoryDTO.builder().build()))
                .build());
        when(academyService.getCourses()).thenReturn(response);

        var actual = mockMvc.perform(get("/dashboard/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt().jwt(c -> c.claim("username", customerId)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var actualResponse = objectMapper.readValue(actual, CourseDTO[].class);

        assertEquals(response.getFirst().getId(), Arrays.stream(actualResponse).findFirst().get().getId());
        verify(academyService).getCourses();
        verifyNoMoreInteractions(academyService);
    }

    @Test
    @WithMockUser(roles = "admin")
    void getChaptersFromCourse() throws Exception {
        var customerId = "04637536374";
        var courseId = 1L;
        var response = List.of(ChapterDTO.builder()
                .chapterId(1L).title("title")
                .build());
        when(academyService.getChaptersFromCourse(courseId)).thenReturn(response);

        var actual = mockMvc.perform(get("/dashboard/chapters/course/"+courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt().jwt(c -> c.claim("username", customerId)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var actualResponse = objectMapper.readValue(actual, ChapterDTO[].class);

        assertEquals(response.getFirst().getChapterId(), Arrays.stream(actualResponse).findFirst().get().getChapterId());
        verify(academyService).getChaptersFromCourse(courseId);
        verifyNoMoreInteractions(academyService);
    }

    @Test
    @WithMockUser(roles = "admin")
    void createCourse() throws Exception {
        var request = CourseRequest.builder()
                .title("Titile")
                .description("Descriptioon")
                .urlImage("url_image")
                .duration("12 min")
                .status(CourseRequest.CourseRequesStatus.AVAILABLE)
                .build();
        doNothing().when(academyService).createCourse(request);

        mockMvc.perform(post("/dashboard/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isAccepted())
                .andReturn();

        verify(academyService).createCourse(request);
        verifyNoMoreInteractions(academyService);
    }

    @Test
    @WithMockUser(roles = "admin")
    void getCoursesFromCategory() throws Exception {
        var customerId = "04637536374";
        var categoryId = 1L;
        var response = List.of(CourseDTO.builder()
                .id(1L).title("title")
                .build());
        when(academyService.getCoursesFromCategory(categoryId)).thenReturn(response);

        var actual = mockMvc.perform(get("/dashboard/courses/category/"+categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt().jwt(c -> c.claim("username", customerId)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var actualResponse = objectMapper.readValue(actual, CourseDTO[].class);

        assertEquals(response.getFirst().getId(), Arrays.stream(actualResponse).findFirst().get().getId());
        verify(academyService).getCoursesFromCategory(categoryId);
        verifyNoMoreInteractions(academyService);
    }

    @Test
    @WithMockUser(roles = "admin")
    void addChapterCourse() throws Exception {
        var request = CourseChapterRequest.builder()
                .id(1L)
                .chapter(List.of(ChapterRequest.builder()
                                .title("title")
                                .description("description")
                                .urlMedia("urlMedia")
                                .duration("duration")
                        .build()))
                .build();
        doNothing().when(academyService).addChapterCourse(request);

        mockMvc.perform(post("/dashboard/chapter/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isAccepted())
                .andReturn();

        verify(academyService).addChapterCourse(request);
        verifyNoMoreInteractions(academyService);
    }

    @Test
    @WithMockUser(roles = "admin")
    void updateCourseInformation() throws Exception {
        var request = CourseRequest.builder()
                .id(1L)
                .title("Titile")
                .description("Descriptioon")
                .urlImage("url_image")
                .duration("12 min")
                .status(CourseRequest.CourseRequesStatus.AVAILABLE)
                .build();
        doNothing().when(academyService).updateCourseInformation(request);

        mockMvc.perform(patch("/dashboard/course/information")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isAccepted())
                .andReturn();

        verify(academyService).updateCourseInformation(request);
        verifyNoMoreInteractions(academyService);
    }

    @Test
    @WithMockUser(roles = "admin")
    void deleteCourse() throws Exception {
        var request = CourseDeleteRequest.builder()
                .id(1L)
                .build();
        doNothing().when(academyService).deleteCourse(request);

        mockMvc.perform(delete("/dashboard/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isAccepted())
                .andReturn();

        verify(academyService).deleteCourse(request);
        verifyNoMoreInteractions(academyService);
    }

    @Test
    @WithMockUser(roles = "admin")
    void updateCourseChapter() throws Exception {
        var request = ChapterRequest.builder()
                .title("title")
                .description("description")
                .urlMedia("urlMedia")
                .duration("duration")
                .build();
        doNothing().when(academyService).updateChapter(request);

        mockMvc.perform(patch("/dashboard/chapters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isAccepted())
                .andReturn();

        verify(academyService).updateChapter(request);
        verifyNoMoreInteractions(academyService);
    }

    @Test
    @WithMockUser(roles = "admin")
    void deleteCourseChapters() throws Exception {
        var request = CourseDeleteRequest.builder()
                .id(1L)
                .build();
        doNothing().when(academyService).deleteCourseChapters(request);

        mockMvc.perform(delete("/dashboard/chapters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isAccepted())
                .andReturn();

        verify(academyService).deleteCourseChapters(request);
        verifyNoMoreInteractions(academyService);
    }
}