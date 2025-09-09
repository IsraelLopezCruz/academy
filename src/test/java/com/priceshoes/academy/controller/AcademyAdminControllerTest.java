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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AcademyAdminController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {MDCFilter.class})}
)
@ImportAutoConfiguration(classes = {Oauth2AuthenticationConfig.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class AcademyAdminControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private AcademyService academyService;


    @Test
    @WithMockUser(roles = "admin")
    void deleteCache() throws Exception {
        doNothing().when(academyService).deleteCache();

        mockMvc.perform(delete("/adm/cache")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isAccepted())
                .andReturn();

        verify(academyService).deleteCache();
        verifyNoMoreInteractions(academyService);
    }
}