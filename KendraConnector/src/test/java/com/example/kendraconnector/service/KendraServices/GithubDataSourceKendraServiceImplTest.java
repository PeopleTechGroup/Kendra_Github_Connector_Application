package com.example.kendraconnector.service.KendraServices;

import com.example.kendraconnector.dto.ResultItemDto;
import com.example.kendraconnector.exceptions.BasicKendraException;
import com.example.kendraconnector.exceptions.DataSourceCreationException;
import com.example.kendraconnector.exceptions.KendraIndexCreationException;
import com.example.kendraconnector.exceptions.KendraQueryException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.kendra.KendraClient;
import software.amazon.awssdk.services.kendra.model.CreateDataSourceRequest;
import software.amazon.awssdk.services.kendra.model.CreateDataSourceResponse;
import software.amazon.awssdk.services.kendra.model.CreateIndexRequest;
import software.amazon.awssdk.services.kendra.model.CreateIndexResponse;
import software.amazon.awssdk.services.kendra.model.DataSourceStatus;
import software.amazon.awssdk.services.kendra.model.DataSourceSummary;
import software.amazon.awssdk.services.kendra.model.DataSourceType;
import software.amazon.awssdk.services.kendra.model.DescribeDataSourceRequest;
import software.amazon.awssdk.services.kendra.model.DescribeDataSourceResponse;
import software.amazon.awssdk.services.kendra.model.DescribeIndexRequest;
import software.amazon.awssdk.services.kendra.model.DescribeIndexResponse;
import software.amazon.awssdk.services.kendra.model.GitHubConfiguration;
import software.amazon.awssdk.services.kendra.model.IndexConfigurationSummary;
import software.amazon.awssdk.services.kendra.model.IndexStatus;
import software.amazon.awssdk.services.kendra.model.ListDataSourcesRequest;
import software.amazon.awssdk.services.kendra.model.ListDataSourcesResponse;
import software.amazon.awssdk.services.kendra.model.ListIndicesRequest;
import software.amazon.awssdk.services.kendra.model.ListIndicesResponse;
import software.amazon.awssdk.services.kendra.model.QueryRequest;
import software.amazon.awssdk.services.kendra.model.QueryResponse;
import software.amazon.awssdk.services.kendra.model.QueryResultItem;
import software.amazon.awssdk.services.kendra.model.SaaSConfiguration;
import software.amazon.awssdk.services.kendra.model.ScoreAttributes;
import software.amazon.awssdk.services.kendra.model.TextWithHighlights;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    //Test for exception handling in createKendraIndex()
    @Test
    void when_createKendraIndexThrowsException_then_itIsHandledProperly() {
        String indexName = "indexName";
        String indexDescription = "indexDescription";
        when(mockKendraClient.createIndex(any(CreateIndexRequest.class)))
                .thenThrow(new RuntimeException("Simulated Exception"));

        assertThrows(KendraIndexCreationException.class, () -> {
            injectedObject.createKendraIndex(indexName, indexDescription);
        });
    }

    //test case for checking Kendra Index Status
    @Test
    void when_checkKendraIndexStatusIsCalled_then_getIndexStatusAsActive() {
        String indexId = "indexId";
        DescribeIndexResponse describeIndexResponse = DescribeIndexResponse.builder().status(IndexStatus.ACTIVE).build();
        when(mockKendraClient.describeIndex(any(DescribeIndexRequest.class)))
                .thenReturn(describeIndexResponse);
        IndexStatus indexStatus = injectedObject.checkKendraIndexStatus(indexId);
        assertEquals(IndexStatus.ACTIVE, indexStatus);
    }

    //Test for exception handling in checkKendraIndexStatus()
    @Test
    void when_checkKendraIndexStatusThrowsException_then_itIsHandledProperly() {
        String indexId = "indexId";
        when(mockKendraClient.describeIndex(any(DescribeIndexRequest.class)))
                .thenThrow(new RuntimeException("Simulated Exception"));
        assertThrows(BasicKendraException.class, () ->{
            injectedObject.checkKendraIndexStatus(indexId);
        });
    }

    //Test cases for checking if kendra index exists
    @Test
    void when_indexIsActive_then_checkKendraIndexExistReturnsTrue() {
        String indexId = "indexId";
        DescribeIndexResponse describeIndexResponse = DescribeIndexResponse.builder().status(IndexStatus.ACTIVE).build();
        when(mockKendraClient.describeIndex(any(DescribeIndexRequest.class))).thenReturn(describeIndexResponse);
        boolean indexExists = injectedObject.checkKendraIndexExists(indexId);
        assertTrue(indexExists);
    }
    @Test
    void when_indexStatusIsFailed_then_checkKendraIndexExistReturnsFalse() {
        String indexId = "indexId";
        DescribeIndexResponse describeIndexResponse = DescribeIndexResponse.builder().status(IndexStatus.FAILED).build();
        when(mockKendraClient.describeIndex(any(DescribeIndexRequest.class))).thenReturn(describeIndexResponse);
        boolean indexExists = injectedObject.checkKendraIndexExists(indexId);
        assertFalse(indexExists);
    }
    @Test
    void when_indexStatusIsFirstCreatingThenActive_then_checkKendraIndexExistReturnsTrue() {
        String indexId = "indexId";
        DescribeIndexResponse describeIndexResponse1 = DescribeIndexResponse.builder().status(IndexStatus.CREATING).build();
        DescribeIndexResponse describeIndexResponse2 = DescribeIndexResponse.builder().status(IndexStatus.ACTIVE).build();
        when(mockKendraClient.describeIndex(any(DescribeIndexRequest.class))).thenReturn(describeIndexResponse1).thenReturn(describeIndexResponse2);
        boolean indexExists = injectedObject.checkKendraIndexExists(indexId);
        assertTrue(indexExists);
    }
    //Test for exception handling in checkKendraIndexExists()
    @Test
    void when_checkKendraIndexExistsThrowsException_then_itIsHandledProperly() {
        String indexId = "indexId";
        when(mockKendraClient.describeIndex(any(DescribeIndexRequest.class)))
                .thenThrow(new RuntimeException("Simulated Exception"));
        assertThrows(KendraIndexCreationException.class, () ->{
            injectedObject.checkKendraIndexExists(indexId);
        });
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
    //Test for exception handling in createKendraGithubDataSource()
    @Test
    void when_createKendraGithubDataSourceThrowsException_then_itIsHandledProperly() {
        String indexId = "indexId";
        when(mockKendraClient.createDataSource(any(CreateDataSourceRequest.class)))
                .thenThrow(new RuntimeException("Simulated Exception"));
        assertThrows(DataSourceCreationException.class, () ->{
            injectedObject.createKendraGithubDataSource(indexId);
        });
    }

    //Test cases for checking if Kendra Data Source Exists
    @Test
    void when_dataSourceStatusIsActive_then_checkKendraDataSourceExistsReturnsTrue() {
        String indexId = "indexId";
        String dataSourceId = "dataSourceId";
        DescribeDataSourceResponse describeDataSourceResponse = DescribeDataSourceResponse.builder().status(DataSourceStatus.ACTIVE).build();
        when(mockKendraClient.describeDataSource(any(DescribeDataSourceRequest.class))).thenReturn(describeDataSourceResponse);
        boolean dataSourceExists = injectedObject.checkKendraDataSourceExists(indexId, dataSourceId);
        assertTrue(dataSourceExists);
    }

    @Test
    void when_dataSourceStatusIsFailed_then_checkKendraDataSourceExistsReturnsFalse() {
        String indexId = "indexId";
        String dataSourceId = "dataSourceId";
        DescribeDataSourceResponse describeDataSourceResponse = DescribeDataSourceResponse.builder().status(DataSourceStatus.FAILED).build();
        when(mockKendraClient.describeDataSource(any(DescribeDataSourceRequest.class))).thenReturn(describeDataSourceResponse);
        boolean dataSourceExists = injectedObject.checkKendraDataSourceExists(indexId, dataSourceId);
        assertFalse(dataSourceExists);
    }

    @Test
    void when_dataSourceStatusIsFirstUpdatingThenActive_then_checkKendraDataSourceExistsReturnsTrue() {
        String indexId = "indexId";
        String dataSourceId = "dataSourceId";
        DescribeDataSourceResponse describeDataSourceResponse1 = DescribeDataSourceResponse.builder().status(DataSourceStatus.UPDATING).build();
        DescribeDataSourceResponse describeDataSourceResponse2 = DescribeDataSourceResponse.builder().status(DataSourceStatus.ACTIVE).build();
        when(mockKendraClient.describeDataSource(any(DescribeDataSourceRequest.class))).thenReturn(describeDataSourceResponse1).thenReturn(describeDataSourceResponse2);
        boolean dataSourceExists = injectedObject.checkKendraDataSourceExists(indexId, dataSourceId);
        assertTrue(dataSourceExists);
    }

    //Test for exception handling in checkKendraDataSourceExists()
    @Test
    void when_checkKendraDataSourceExistsThrowsException_then_itIsHandledProperly() {
        String indexId = "indexId";
        String dataSourceId = "dataSourceId";
        when(mockKendraClient.describeDataSource(any(DescribeDataSourceRequest.class)))
                .thenThrow(new RuntimeException("Simulated Exception"));
        assertThrows(DataSourceCreationException.class, () ->{
            injectedObject.checkKendraDataSourceExists(indexId, dataSourceId);
        });
    }

    //test case for checking query results
    @Test
    void when_getQueryResultIsCalled_then_getExpectedListOfQueryResultItems() {
        String id = "id";
        String type = "type";
        String format = "format";
        String documentTitleText = "documentTitle";
        TextWithHighlights documentTitle = TextWithHighlights.builder().text(documentTitleText).build();
        String documentExcerptText = "documentExcerpt";
        TextWithHighlights documentExcerpt = TextWithHighlights.builder().text(documentExcerptText).build();
        String documentId = "documentId";
        String documentURI = "documentURI";
        String scoreConfidence = "scoreConfidence";
        ScoreAttributes scoreAttributes = ScoreAttributes.builder().scoreConfidence(scoreConfidence).build();
        String query = "query";
        String indexId = "indexId";
        List<QueryResultItem> resultItems = new ArrayList<>();
        QueryResultItem queryResultItem =  QueryResultItem.builder()
                .id(id)
                .type(type)
                .format(format)
                .documentTitle(documentTitle)
                .documentExcerpt(documentExcerpt)
                .documentId(documentId)
                .documentURI(documentURI)
                .scoreAttributes(scoreAttributes)
                .build();
        resultItems.add(queryResultItem);
        QueryResponse queryResponse = QueryResponse.builder().resultItems(resultItems).build();
        when(mockKendraClient.query(any(QueryRequest.class))).thenReturn(queryResponse);
        List<com.example.kendraconnector.model.QueryResultItem> queryResultItemsLocal = injectedObject.getQueryResult(query, indexId);

        List<com.example.kendraconnector.model.QueryResultItem> queryResultItemsLocalExpected = resultItems.stream()
                .map(resultItem -> {
                    com.example.kendraconnector.model.QueryResultItem model = new com.example.kendraconnector.model.QueryResultItem();
                    model.setId(resultItem.id());
                    model.setType(resultItem.typeAsString());
                    model.setFormat(resultItem.formatAsString());
                    model.setDocumentTitle(resultItem.documentTitle().text());
                    model.setDocumentExcerpt(resultItem.documentExcerpt().text());
                    model.setDocumentId(resultItem.documentId());
                    model.setDocumentURI(resultItem.documentURI());
                    model.setScoreAttributes(resultItem.scoreAttributes().scoreConfidenceAsString());
                    return model;
                })
                .toList();

        assertEquals(queryResultItemsLocalExpected, queryResultItemsLocal);
    }

    //Test for exception handling in checkKendraDataSourceExists()
    @Test
    void when_getQueryResultThrowsException_then_itIsHandledProperly() {
        String indexId = "indexId";
        String dataSourceId = "dataSourceId";
        when(mockKendraClient.query(any(QueryRequest.class)))
                .thenThrow(new RuntimeException("Simulated Exception"));
        assertThrows(KendraQueryException.class, () ->{
            injectedObject.getQueryResult(indexId, dataSourceId);
        });
    }

    //Test cases for getting all data sources in an index
    @Test
    void when_indexIdIsProvided_then_getAllItsDataSources() {
        String id = "id";
        DataSourceType type = DataSourceType.S3;
        String indexId = "indexId";
        List<DataSourceSummary> dataSourceSummary = new ArrayList<>();
        DataSourceSummary dataSourceSummary1 = DataSourceSummary.builder().id(id).type(type).build();
        dataSourceSummary.add(dataSourceSummary1);
        ListDataSourcesResponse listDataSourcesResponse = ListDataSourcesResponse.builder().summaryItems(dataSourceSummary).build();
        when(mockKendraClient.listDataSources(any(ListDataSourcesRequest.class))).thenReturn(listDataSourcesResponse);
        List<Pair<String,String>> allDataSources = injectedObject.getAllDataSources(indexId);
        List<Pair<String,String>> allDataSourcesExpected = new ArrayList<>();
        allDataSourcesExpected.add(new ImmutablePair<>(id, type.toString()));
        assertEquals(allDataSourcesExpected, allDataSources);
    }

    //Test for exception handling in getAllDataSources()
    @Test
    void when_getAllDataSourcesException_then_itIsHandledProperly() {
        String indexId = "indexId";
//        String dataSourceId = "dataSourceId";
        when(mockKendraClient.listDataSources(any(ListDataSourcesRequest.class)))
                .thenThrow(new RuntimeException("Simulated Exception"));
        assertThrows(BasicKendraException.class, () ->{
            injectedObject.getAllDataSources(indexId);
        });
    }

    //Test cases for getting all Kendra Indexes
    @Test
    void when_getAllKendraIndexesIsCalled_then_getAllKendraIndexes() {
        String indexId = "indexId";
        String indexName = "indexName";
        List<IndexConfigurationSummary> indexConfigurationSummary = new ArrayList<>();
        IndexConfigurationSummary indexConfigurationSummary1 = IndexConfigurationSummary.builder().id(indexId).name(indexName).build();
        indexConfigurationSummary.add(indexConfigurationSummary1);
        ListIndicesResponse listIndicesResponse = ListIndicesResponse.builder().indexConfigurationSummaryItems(indexConfigurationSummary).build();
        when(mockKendraClient.listIndices(any(ListIndicesRequest.class))).thenReturn(listIndicesResponse);
        List<Pair<String,String>> allKendraIndexes = injectedObject.getAllKendraIndexes();
        List<Pair<String,String>> allKendraIndexesExpected = new ArrayList<>();
        allKendraIndexesExpected.add(new ImmutablePair<>(indexId, indexName));
        assertEquals(allKendraIndexesExpected, allKendraIndexes);
    }

    //Test for exception handling in getAllKendraIndexes()
    @Test
    void when_getAllKendraIndexesException_then_itIsHandledProperly() {
        when(mockKendraClient.listIndices(any(ListIndicesRequest.class)))
                .thenThrow(new RuntimeException("Simulated Exception"));
        assertThrows(BasicKendraException.class, () ->{
            injectedObject.getAllKendraIndexes();
        });
    }

    //test case for createDataSourceConfiguration()
    @Test
    void when_createDataSourceConfigurationIsCalled_then_itReturnExpectedString() {
        // Arrange
        String secretArn = "secretArn";
        String organizationName = "organizationName";
        String hostUrl = "hostUrl";

        ReflectionTestUtils.setField(injectedObject, "secretArn", secretArn);
        ReflectionTestUtils.setField(injectedObject, "organizationName", organizationName);
        ReflectionTestUtils.setField(injectedObject, "hostUrl", hostUrl);

        String dataSourceConfiguration = injectedObject.createDataSourceConfiguration();
        String dataSourceConfigurationExpected = String.format("DataSourceConfiguration(GitHubConfiguration=GitHubConfiguration(SaaSConfiguration=SaaSConfiguration(OrganizationName=%s, HostUrl=%s), SecretArn=%s))", organizationName, hostUrl, secretArn);
        assertEquals(dataSourceConfigurationExpected, dataSourceConfiguration);

    }
}