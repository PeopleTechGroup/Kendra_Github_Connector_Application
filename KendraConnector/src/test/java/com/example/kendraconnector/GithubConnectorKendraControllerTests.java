package com.example.kendraconnector;
import org.apache.commons.lang3.tuple.Pair;

import com.example.kendraconnector.controller.GithubConnectorKendraController;
import com.example.kendraconnector.dto.ResultItemDto;
import com.example.kendraconnector.model.QueryResultItem;
import com.example.kendraconnector.service.KendraServices.GithubDataSourceKendraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.apache.commons.lang3.StringUtils;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GithubConnectorKendraControllerTests {

    @InjectMocks
    private GithubConnectorKendraController controller;

    @Mock
    private GithubDataSourceKendraService githubDataSourceKendraService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateIndex() throws InterruptedException {
        // Mock the service method
        String indexName = "TestIndex";
        String description = "TestDescription";
        ResultItemDto expectedResult = new ResultItemDto.Builder()
                .indexName(indexName)
                .description(description)
                .build();
        when(githubDataSourceKendraService.createKendraIndex(indexName, description)).thenReturn(expectedResult);

        // Test the controller method
        ResponseEntity<ResultItemDto> response = controller.createIndex(indexName, description);

        // Verify that the service method was called with the expected parameters
        verify(githubDataSourceKendraService).createKendraIndex(indexName, description);

        // Verify the response
        assertEquals(expectedResult, response.getBody());
    }

    // Similar test methods for other controller endpoints
    @Test
    public void testCheckIndexExists() {
        // Mock the service method
        String indexId = "TestIndexId";
        Boolean indexExists = true;

        when(githubDataSourceKendraService.checkKendraIndexExists(indexId)).thenReturn(indexExists);

        // Test the controller method
        ResponseEntity<Boolean> response = controller.checkIndexExists(indexId);

        // Verify that the service method was called with the expected parameter
        verify(githubDataSourceKendraService).checkKendraIndexExists(indexId);

        // Verify the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify the response body
        assertEquals(indexExists, response.getBody());
    }
    @Test
    public void testGetAllAvailableIndexes() {
        // Mock the service method
        List<Pair<String, String>> indicesInfo = Arrays.asList(
                Pair.of("Index1", "Description1"),
                Pair.of("Index2", "Description2")
        );

        when(githubDataSourceKendraService.getAllKendraIndexes()).thenReturn(indicesInfo);

        // Test the controller method
        ResponseEntity<List<Pair<String, String>>> response = controller.getAllAvailableIndexes();

        // Verify that the service method was called
        verify(githubDataSourceKendraService).getAllKendraIndexes();

        // Verify the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify the response body
        assertEquals(indicesInfo, response.getBody());
    }

    @Test
    public void testCreateDataSourceId() {
        // Mock the service method
        String indexId = "TestIndexId";
        String dataSourceId = "TestDataSourceId";

        when(githubDataSourceKendraService.createKendraGithubDataSource(indexId)).thenReturn(dataSourceId);

        // Test the controller method
        String response = controller.createDataSourceId(indexId);

        // Verify that the service method was called with the expected parameter
        verify(githubDataSourceKendraService).createKendraGithubDataSource(indexId);

        // Verify the response
        assertEquals(dataSourceId, response);
    }

    @Test
    public void testCheckKendraDataSourceExists() {
        // Mock the service method
        String indexId = "TestIndexId";
        String dataSourceType = "TestDataSourceType";
        Boolean dataSourceExists = true;

        when(githubDataSourceKendraService.checkKendraDataSourceExists(indexId, dataSourceType)).thenReturn(dataSourceExists);

        // Test the controller method
        ResponseEntity<Boolean> response = controller.checkKendraDataSourceExists(indexId, dataSourceType);

        // Verify that the service method was called with the expected parameters
        verify(githubDataSourceKendraService).checkKendraDataSourceExists(indexId, dataSourceType);

        // Verify the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify the response body
        assertEquals(dataSourceExists, response.getBody());
    }

    @Test
    public void testGetDataSources() {
        // Mock the service method
        String indexId = "TestIndexId";
        List<Pair<String, String>> dataSources = Arrays.asList(
                Pair.of("DataSource1", "Type1"),
                Pair.of("DataSource2", "Type2")
        );

        when(githubDataSourceKendraService.getAllDataSources(indexId)).thenReturn(dataSources);

        // Test the controller method
        ResponseEntity<List<org.apache.commons.lang3.tuple.Pair<String, String>>> response = controller.getDataSources(indexId);

        // Verify that the service method was called with the expected parameter
        verify(githubDataSourceKendraService).getAllDataSources(indexId);

        // Verify the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify the response body
        assertEquals(dataSources, response.getBody());
    }

    @Test
    public void testGetSearchResultsFromQuery() {
        // Mock the service method
        String query = "TestQuery";
        String indexId = "TestIndexId";
        List<QueryResultItem> queryResult = Arrays.asList(
                new QueryResultItem(),
                new QueryResultItem()
        );

        when(githubDataSourceKendraService.getQueryResult(query, indexId)).thenReturn(queryResult);

        // Test the controller method
        ResponseEntity<List<QueryResultItem>> response = controller.getSearchResultsFromQuery(query, indexId);

        // Verify that the service method was called with the expected parameters
        verify(githubDataSourceKendraService).getQueryResult(query, indexId);

        // Verify the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify the response body
        assertEquals(queryResult, response.getBody());
    }

    @Test
    public void testGetSaasConfigurationDetails() {
        // Mock the service method
        String organizationName = "TestOrganization";

        when(githubDataSourceKendraService.createDataSourceConfiguration()).thenReturn(organizationName);

        // Test the controller method
        ResponseEntity<String> response = controller.getSaasConfigurationDetails();

        // Verify that the service method was called
        verify(githubDataSourceKendraService).createDataSourceConfiguration();

        // Verify the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify the response body
        assertEquals(organizationName, response.getBody());
    }
}
