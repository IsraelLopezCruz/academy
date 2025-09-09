package com.priceshoes.academy;

import com.priceshoes.academy.configuration.AcademyConfiguration;
import com.priceshoes.academy.configuration.DashboardPsAcademyConfig;
import com.priceshoes.academy.configuration.OAuthIssuersConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties({
		OAuthIssuersConfig.class, AcademyConfiguration.class, DashboardPsAcademyConfig.class
})
public class AcademyApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(AcademyApplication.class, args);
	}
	
}