package com.example.kendraconnector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.kendraconnector.config.AWSConfig;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.services.kendra.KendraClient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AWSConfigTest {

    @InjectMocks
    private AWSConfig awsConfig;

    @Mock
    private AwsCredentials awsCredentials;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Set values using ReflectionTestUtils, especially useful for private fields
        ReflectionTestUtils.setField(awsConfig, "accessKey", "testAccessKey");
        ReflectionTestUtils.setField(awsConfig, "secretKey", "testSecretKey");
        ReflectionTestUtils.setField(awsConfig, "region", "us-west-1");
        ReflectionTestUtils.setField(awsConfig, "indexRoleArn", "testRoleArn");
    }

    @Test
    public void testKendraClient() {
        when(awsCredentials.accessKeyId()).thenReturn("testAccessKey");
        when(awsCredentials.secretAccessKey()).thenReturn("testSecretKey");

        KendraClient client = awsConfig.kendraClient();

        assertNotNull(client);
        // Add more assertions if necessary to verify properties of the KendraClient
    }

    @Test
    public void testAwsCredentialsFormat() {
        String accessKey = (String) ReflectionTestUtils.getField(awsConfig, "accessKey");
        String secretKey = (String) ReflectionTestUtils.getField(awsConfig, "secretKey");

        assertNotNull(accessKey, "AccessKey should not be null");
        assertFalse(accessKey.isEmpty(), "AccessKey should not be empty");
        assertNotNull(secretKey, "SecretKey should not be null");
        assertFalse(secretKey.isEmpty(), "SecretKey should not be empty");
    }

    @Test
    public void testAwsRegionFormat() {
        String region = (String) ReflectionTestUtils.getField(awsConfig, "region");

        // The pattern roughly matches AWS region formats.
        Pattern pattern = Pattern.compile("^([a-z]{2}-[a-z]+-[1-9]{1}[0-9]*)$");
        Matcher matcher = pattern.matcher(region);

        assertTrue(matcher.find(), "Invalid AWS region format");
    }


}
