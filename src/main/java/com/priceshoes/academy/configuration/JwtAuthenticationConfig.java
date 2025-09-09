package com.priceshoes.academy.configuration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class JwtAuthenticationConfig {
    @NonNull
    private JwtIssuerAuthenticationManagerResolver jwtIssuerAuthenticationManagerResolver;
    @NonNull
    private DashboardPsAcademyConfig dashboardPsAcademyConfig;


    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/dashboard/**")
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .csrf(CsrfConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(Customizer.withDefaults())
                .authorizeHttpRequests(expressionInterceptUrlRegistry -> expressionInterceptUrlRegistry.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.authenticationManagerResolver(jwtIssuerAuthenticationManagerResolver));
        http.cors(Customizer.withDefaults());
        http.headers(Customizer.withDefaults());
        return http.build();
    }

    private Converter<Jwt, ? extends AbstractAuthenticationToken> grantedAuthoritiesExtractor() {
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("cognito:groups");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public WebSecurityCustomizer webSecurity() {
        return web -> web.ignoring().requestMatchers(HttpMethod.GET,"/actuator/health", "/actuator/info", "/api-docs*", "/lp/evict-landing-and-home");
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDashBoard = User.builder()
                .username(dashboardPsAcademyConfig.getUser())
                .password(passwordEncoder().encode(dashboardPsAcademyConfig.getPass()))
                .build();
        return new InMemoryUserDetailsManager(userDashBoard);
    }
}