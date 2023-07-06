package com.example.kendraconnector.controller;

import com.example.kendraconnector.dto.ResultItemDto;
import com.example.kendraconnector.model.QueryResultItem;
import com.example.kendraconnector.service.KendraServices.GithubDataSourceKendraService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.kendra.model.IndexStatus;

import java.util.List;

@RestController("api/v1/github-connector")
public class GithubConnectorKendraController {

    @Autowired
    private GithubDataSourceKendraService githubDataSourceKendraService;

    @PostMapping("/create/index")
    public ResponseEntity<ResultItemDto> createIndex(@RequestHeader("indexName") String indexName, @RequestHeader("description") String description) throws InterruptedException {
        ResultItemDto response = githubDataSourceKendraService.createKendraIndex(indexName, description);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/index/status")
    public ResponseEntity<IndexStatus> checkIndexStatus(@RequestHeader("indexId") String indexId) {
        /* indexId - Kendra Index created. */

        IndexStatus indexStatus = githubDataSourceKendraService.checkKendraIndexStatus(indexId);
        return ResponseEntity.ok(indexStatus);
    }

    @GetMapping("/index/exists")
    public ResponseEntity<Boolean> checkIndexExists(@RequestHeader("indexId") String indexId) {
        /* indexId - Kendra Index created. */

        Boolean indexStatus = githubDataSourceKendraService.checkKendraIndexExists(indexId);
        return ResponseEntity.ok(indexStatus);
    }

    @GetMapping("/indexes")
    public ResponseEntity<List<Pair<String,String>>> getAllAvailableIndexes() {
        List<Pair<String,String>> indicesInfo = githubDataSourceKendraService.getAllKendraIndexes();
        return ResponseEntity.ok(indicesInfo);
    }

    @PostMapping("/createDatasource")
    public String createDataSourceId(@RequestHeader("indexId") String indexId) {
        /* indexId - Kendra Index created. */

        return githubDataSourceKendraService.createKendraGithubDataSource(indexId);
    }

    @GetMapping("/datasource/exists")
    public ResponseEntity<Boolean> checkKendraDataSourceExists(@RequestHeader("indexId") String indexId, @RequestHeader("dataSourceId") String dataSourceType) {

        /*

           indexId - Kendra Index created.
           dataSourceId - Kendra DataSource created and mapped through programmatically / AWS Console.

        */

        Boolean dataSourceStatus = githubDataSourceKendraService.checkKendraDataSourceExists(indexId,dataSourceType);
        return ResponseEntity.ok(dataSourceStatus);
    }

    @GetMapping("/datasource/all")
    public ResponseEntity<List<Pair<String,String>>> getDataSources(@RequestHeader("indexId") String indexId) {
        List<Pair<String,String>> dataSources = githubDataSourceKendraService.getAllDataSources(indexId);
        return ResponseEntity.ok(dataSources);
    }

    @GetMapping("/search")
    public ResponseEntity<List<QueryResultItem>> getSearchResultsFromQuery(@RequestHeader("query") String query, @RequestHeader("indexId") String indexId) {
        /*

           indexId - Kendra Index created.
           query - Search query processed through Kendra index.

        */
        List<QueryResultItem> queryResult = githubDataSourceKendraService.getQueryResult(query, indexId);
        return ResponseEntity.ok(queryResult);
    }

    @GetMapping("/dataSourceConfiguration")
    public ResponseEntity<String> getSaasConfigurationDetails() {
        String organizationName =  githubDataSourceKendraService.createDataSourceConfiguration();
        return ResponseEntity.ok(organizationName);
    }
}
