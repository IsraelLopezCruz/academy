package com.priceshoes.academy.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

import java.net.URI;
import java.util.List;

import static io.awspring.cloud.sns.configuration.NotificationHandlerMethodArgumentResolverConfigurationUtils.getNotificationHandlerMethodArgumentResolver;


@Configuration
public class AwsSnsConfig implements WebMvcConfigurer {
	@Value("${aws.local.endpoint}")
	private String awsLocalEndpoint;

	@Value("${aws.local.enabled}")
	private boolean awsLocalEnabled;
	@Value("${AWS_REGION:us-east-1}")
	private String region;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(getNotificationHandlerMethodArgumentResolver(amazonSNSClient()));
	}

	@Primary
	@Bean
	public SnsClient amazonSNSClient() {
		if (awsLocalEnabled) {
			return SnsClient.builder().region(Region.of(region))
					.endpointOverride(URI.create(awsLocalEndpoint)).build();
		} else {
			return SnsClient.builder()
					.build();
		}
	}


}
