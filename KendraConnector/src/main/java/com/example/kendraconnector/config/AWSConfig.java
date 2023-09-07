package com.example.kendraconnector.config;

import com.example.kendraconnector.exceptions.AWSConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kendra.KendraClient;

@Configuration
public class AWSConfig {

    @Value("${aws.githubProfile3.credentials.accessKey:}")
    private String accessKey;

    @Value("${aws.githubProfile3.credentials.secretKey:}")
    private String secretKey;

    @Value("${aws.githubProfile3.credentials.region:}")
    private String region;

    @Value("${aws.githubProfile3.iamRole.arn:}")
    private String indexRoleArn;

    @Bean
    public KendraClient kendraClient() {
        try {
            // Validate that required properties exist
            if (accessKey.isEmpty() || secretKey.isEmpty() || region.isEmpty()) {
                throw new AWSConfigurationException("AWS credentials and region must be specified.");
            }

            AwsCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

            return KendraClient
                    .builder()
                    .region(Region.of(region))
                    .credentialsProvider(() -> awsCredentials)
                    .build();

        } catch (SdkClientException e) {
            // AWS SDK exceptions (e.g., can't connect to service, credentials are invalid, etc.)
            throw new AWSConfigurationException("Failed to initialize Kendra client", e);
        }
    }
}
