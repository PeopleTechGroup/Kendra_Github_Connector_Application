// package com.example.kendraconnector;

// import com.example.kendraconnector.config.GithubConfig;
// import com.example.kendraconnector.exceptions.BasicKendraException;
// import com.example.kendraconnector.exceptions.DataSourceCreationException;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.TestPropertySource;
// import org.springframework.test.util.ReflectionTestUtils;

// import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest
// @TestPropertySource(properties = {
//         "aws.githubProfile.secret.arn=testArn",
//         "aws.githubProfile.hostUrl=testUrl",
//         "aws.githubProfile.organizationName=testOrg"
// })
// public class GithubConfigTest {

//     @Autowired
//     GithubConfig githubConfig;

//     @Test
//     void testGetGitHubConfig_HappyPath() {
//         assertNotNull(githubConfig.getGitHubConfig());
//     }

//     @Test
//     void testGetGitHubConfig_MissingSecretArn_ThrowsException() {
//         ReflectionTestUtils.setField(githubConfig, "secretArn", null);
//         assertThrows(BasicKendraException.class, () -> githubConfig.getGitHubConfig());
//     }

//     @Test
//     void testGetGitHubConfig_MissingHostUrl_ThrowsException() {
//         ReflectionTestUtils.setField(githubConfig, "hostUrl", null);
//         assertThrows(BasicKendraException.class, () -> githubConfig.getGitHubConfig());
//     }

//     @Test
//     void testGetGitHubConfig_MissingOrganizationName_ThrowsException() {
//         ReflectionTestUtils.setField(githubConfig, "organizationName", null);
//         assertThrows(BasicKendraException.class, () -> githubConfig.getGitHubConfig());
//     }

//     @Test
//     void testGetGitHubConfig_DataSourceCreationException_ThrownWhenExceptionOccurs() {
//         ReflectionTestUtils.setField(githubConfig, "hostUrl", "invalid_url");
//         assertThrows(DataSourceCreationException.class, () -> githubConfig.getGitHubConfig());
//     }
// }
