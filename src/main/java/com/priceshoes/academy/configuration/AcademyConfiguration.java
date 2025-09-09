package com.priceshoes.academy.configuration;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;


@RequiredArgsConstructor
@Getter
@ConfigurationProperties(prefix = "academy-config")
public class AcademyConfiguration {
    @NonNull
    private final Duration cacheTtl;
}
