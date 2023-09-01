package com.example.kendraconnector.service.KendraServices;

import com.example.kendraconnector.dto.ResultItemDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.kendra.KendraClient;
import software.amazon.awssdk.services.kendra.model.CreateIndexRequest;
import software.amazon.awssdk.services.kendra.model.CreateIndexResponse;
import software.amazon.awssdk.services.kendra.model.GitHubConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class GithubDataSourceKendraServiceImplTest {

    //created mock instances of KendraClient and GitHubConfiguration.
    @Mock
    private KendraClient mockKendraClient;

    @Mock
    private GitHubConfiguration mockGitHubConfiguration;

    //@InjectMocks is used to inject the mocks created with @Mock into the class under test (GithubDataSourceKendraServiceImpl).
    @InjectMocks
    private GithubDataSourceKendraServiceImpl injectedObject;

    //@BeforeEach , the MockitoAnnotations.initMocks(this): initializing the mock objects and injects them into the test class.
    // ensuring that the mock objects are ready for use in each test method.
    @BeforeEach
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void when_createKendraIndexIsCalled_then_getCorrectId(){
        //creates a mock CreateIndexResponse with a sample index ID
        CreateIndexResponse createIndexResponse  = CreateIndexResponse.builder()
                .id("1234")
                .build();
        //using Mockito's when method to define the behavior of the mockKendraClient when the createIndex method is called with any CreateIndexRequest.
        when(mockKendraClient.createIndex(any(CreateIndexRequest.class))).thenReturn(createIndexResponse);
        ResultItemDto resultItemDto = injectedObject.createKendraIndex("name", "description");
        //assertEquals assertion ensures that the expected and actual values match.
        assertEquals("1234", resultItemDto.getId());
    }

    //Test case for checking index description when createKendraIndex is called
    @Test
    void when_createKendraIndexIsCalled_then_getCorrectIndexDescription() {
        CreateIndexResponse createIndexResponse = CreateIndexResponse.builder().build();
        when(mockKendraClient.createIndex(any(CreateIndexRequest.class))).thenReturn(createIndexResponse);
        ResultItemDto resultItemDto = injectedObject.createKendraIndex("sinka", "my name");
        assertEquals("my name", resultItemDto.getDescription());
    }

    //Test case for checking if kendraIndex exists

}