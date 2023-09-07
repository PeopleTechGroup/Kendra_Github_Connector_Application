package com.example.kendraconnector.config;

import com.example.kendraconnector.exceptions.BasicKendraException;
import com.example.kendraconnector.exceptions.DataSourceCreationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.kendra.model.GitHubConfiguration;
import software.amazon.awssdk.services.kendra.model.SaaSConfiguration;

@Configuration
public class GithubConfig {

    @Value("${aws.githubProfile.secret.arn}")
    private String secretArn;

    @Value("${aws.githubProfile.hostUrl}")
    private String hostUrl;

    @Value("${aws.githubProfile.organizationName}")
    private String organizationName;

    @Bean
    public GitHubConfiguration getGitHubConfig() {
        try {
            if (secretArn == null || hostUrl == null || organizationName == null) {
                throw new BasicKendraException("Required properties for GitHubConfiguration are missing");
            }

            return GitHubConfiguration.builder()
                    .saaSConfiguration(SaaSConfiguration.builder()
                            .hostUrl(hostUrl) //https://api.github.com
                            .organizationName(organizationName)
                            .build())
                    .secretArn(secretArn)
                    .build();

        } catch (Exception e) {
            throw new DataSourceCreationException("Failed to create GitHubConfiguration bean: " + e.getMessage());
        }
    }
}
