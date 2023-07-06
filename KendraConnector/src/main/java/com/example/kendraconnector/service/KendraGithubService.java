package com.example.kendraconnector.service;

import com.example.kendraconnector.model.IndexCreationResultItem;
import com.example.kendraconnector.model.QueryResultItem;
import com.example.kendraconnector.service.KendraServices.GithubDataSourceKendraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.kendra.KendraClient;
import software.amazon.awssdk.services.kendra.model.IndexStatus;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KendraGithubService {

    private final KendraClient kendraClient;

    private final IndexSearchService indexSearchService;
    private final CreateNewIndexService createNewIndexService;
    private final PollService pollService;

    private final GithubDataSourceKendraService githubDataSourceKendraService;

    private String kendraIndexId = "c08e81d6-310f-464e-9d6c-7a681189253e";


    public List<QueryResultItem> getQueryResult(String queryText, String indexId){
        return indexSearchService.searchIndex(queryText, indexId, kendraClient);
    }

    public IndexCreationResultItem createIndex(String indexName, String indexDescription) throws InterruptedException {
        return createNewIndexService.createIndexInKendra(indexName, indexDescription, kendraClient);
    }

    public IndexStatus checkIndex(String indexId) {
        return pollService.checkIfIndexIsCreated(kendraClient, indexId);
    }

}
