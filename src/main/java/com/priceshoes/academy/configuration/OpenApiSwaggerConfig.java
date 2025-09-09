package com.priceshoes.academy.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiSwaggerConfig {
	
	@Value("${springdoc.server_url}")
	private String serverUrl;
	
	@Value("${springdoc.api_version}")
	private String apiVersion;
	
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().addServersItem(new Server().url(serverUrl))
                .info(new Info().title("Academy API").version(apiVersion)
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    } 

}
