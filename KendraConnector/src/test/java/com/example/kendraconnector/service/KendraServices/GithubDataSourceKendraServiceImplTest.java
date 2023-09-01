package com.example.kendraconnector.service.KendraServices;

import com.example.kendraconnector.dto.ResultItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.kendra.KendraClient;
import software.amazon.awssdk.services.kendra.model.CreateDataSourceRequest;
import software.amazon.awssdk.services.kendra.model.CreateDataSourceResponse;
import software.amazon.awssdk.services.kendra.model.CreateIndexRequest;
import software.amazon.awssdk.services.kendra.model.CreateIndexResponse;
import software.amazon.awssdk.services.kendra.model.DescribeIndexRequest;
import software.amazon.awssdk.services.kendra.model.DescribeIndexResponse;
import software.amazon.awssdk.services.kendra.model.GitHubConfiguration;
import software.amazon.awssdk.services.kendra.model.IndexStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void when_createKendraIndexIsCalled_then_getCorrectId() {
        //creates a mock CreateIndexResponse with a sample index ID
        String indexId = "indexId";
        CreateIndexResponse createIndexResponse = CreateIndexResponse.builder()
                .id(indexId)
                .build();
        //using Mockito's when method to define the behavior of the mockKendraClient when the createIndex method is called with any CreateIndexRequest.
        when(mockKendraClient.createIndex(any(CreateIndexRequest.class))).thenReturn(createIndexResponse);
        ResultItemDto resultItemDto = injectedObject.createKendraIndex("name", "description");
        //assertEquals assertion ensures that the expected and actual values match.
        assertEquals(indexId, resultItemDto.getId());
    }

    //Test case for checking index description when createKendraIndex is called
    @Test
    void when_createKendraIndexIsCalled_then_getCorrectIndexDescription() {
        String indexName = "indexName";
        String indexDescription = "indexDescription";
        CreateIndexResponse createIndexResponse = CreateIndexResponse.builder().build();
        when(mockKendraClient.createIndex(any(CreateIndexRequest.class))).thenReturn(createIndexResponse);
        ResultItemDto resultItemDto = injectedObject.createKendraIndex(indexName, indexDescription);
        assertEquals(indexDescription, resultItemDto.getDescription());
    }

    //test case for checking Kendra Index Status
    @Test
    void when_checkKendraIndexStatusIsCalled_then_getIndexStatusAsActive(){
        String indexId = "indexId";
        DescribeIndexResponse describeIndexResponse = DescribeIndexResponse.builder().status(IndexStatus.ACTIVE).build();
        when(mockKendraClient.describeIndex(any(DescribeIndexRequest.class)))
                .thenReturn(describeIndexResponse);
        IndexStatus indexStatus = injectedObject.checkKendraIndexStatus(indexId);
        assertEquals(IndexStatus.ACTIVE, indexStatus);
    }

    //Test case for creating GitHub data source associated with Kendra Index
    @Test
    void when_createKendraGithubDataSourceIsCalled_then_getIndexId() {
        String indexId = "indexId";
        CreateDataSourceResponse createDataSourceResponse = CreateDataSourceResponse.builder().id(indexId).build();
        when(mockKendraClient.createDataSource(any(CreateDataSourceRequest.class))).thenReturn(createDataSourceResponse);
        String result = injectedObject.createKendraGithubDataSource(indexId);
        assertEquals(indexId, result);
    }



}
