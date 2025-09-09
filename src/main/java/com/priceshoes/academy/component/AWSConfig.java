package com.priceshoes.academy.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class AWSConfig {
    @Value("${aws.local.endpoint}")
    private String awsLocalEndPoint;

    @Value("${aws.local.enabled}")
    private boolean awsLocalEnabled;

    public SnsClient getSnsClient() throws URISyntaxException {
        return applyOverride(SnsClient.builder()).build();
    }

    public SqsClient getSqsClient() throws URISyntaxException {
        return applyOverride(SqsClient.builder()).build();
    }

    private <B extends AwsClientBuilder<B, C>, C> AwsClientBuilder<B, C>
    applyOverride(AwsClientBuilder<B, C> builder) throws URISyntaxException {
        if (awsLocalEnabled) {
            builder.endpointOverride(new URI(awsLocalEndPoint));
        }
        return builder;
    }
}
