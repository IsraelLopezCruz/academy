package com.priceshoes.academy.configuration;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;


@Configuration(proxyBeanMethods = false)
@Slf4j
@AllArgsConstructor
public class RedisCacheManagerConfig {

    public static final String CATEGORIES = "categories";
    public static final String COURSES_FOR_CUSTOMER = "courses-for-customer";
    public static final String COURSE_FOR_CUSTOMER = "course-for-customer";
    public static final String COURSES = "courses";
    public static final String COURSES_FROM_CATEGORY = "courses-from-category";
    public static final String CHAPTERS_FROM_COURSE = "chapters-from-course";

    @NonNull
    private final AcademyConfiguration academyConfiguration;

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManager() {
        return builder -> builder
                .withCacheConfiguration(CATEGORIES,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(academyConfiguration.getCacheTtl()))
                .withCacheConfiguration(COURSES_FOR_CUSTOMER,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(academyConfiguration.getCacheTtl()))
                .withCacheConfiguration(COURSE_FOR_CUSTOMER,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(academyConfiguration.getCacheTtl()))
                .withCacheConfiguration(COURSES,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(academyConfiguration.getCacheTtl()))
                .withCacheConfiguration(COURSES_FROM_CATEGORY,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(academyConfiguration.getCacheTtl()))
                .withCacheConfiguration(CHAPTERS_FROM_COURSE,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(academyConfiguration.getCacheTtl()));
    }

}
