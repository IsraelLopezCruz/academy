package com.priceshoes.academy.configuration;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@RequiredArgsConstructor
@Getter
@ConfigurationProperties(prefix = "dashboard-ps-academy")
public class DashboardPsAcademyConfig {

    @NonNull
    private final String user;
    @NonNull
    private final String pass;


}

