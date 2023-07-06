package com.example.kendraconnector.config;

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
        return GitHubConfiguration.builder()
                .saaSConfiguration(SaaSConfiguration.builder()
                        .hostUrl(hostUrl) //https://api.github.com
                        .organizationName(organizationName)
                        .build())
                .secretArn(secretArn)
                .build();
    }

}
