package com.example.kendraconnector.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kendra.KendraClient;

@Configuration
public class AWSConfig {

    @Value("${aws.githubProfile3.credentials.accessKey}")
    private String accessKey;

    @Value("${aws.githubProfile3.credentials.secretKey}")
    private String secretKey;

    @Value("${aws.githubProfile3.credentials.region}")
    private String region;

    @Value("${aws.githubProfile3.iamRole.arn}")
    private String indexRoleArn;

    @Bean
    public KendraClient kendraClient() {
        AwsCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        return KendraClient
                .builder()
                .region(Region.of(region))
                .credentialsProvider(() -> awsCredentials)
                .build();
    }
}
