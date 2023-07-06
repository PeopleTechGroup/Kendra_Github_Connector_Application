package com.example.kendraconnector.service;

import com.example.kendraconnector.model.IndexCreationResultItem;
import com.example.kendraconnector.model.QueryResultItem;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kendra.KendraClient;
import software.amazon.awssdk.services.kendra.model.IndexStatus;

import java.util.List;

@Service
@Component
public class KendraService {
    @Value("${aws.credentials.accessKey}")
    private String accessKey;

    @Value("${aws.credentials.secretKey}")
    private String secretKey;
    private final IndexSearchService indexSearchService;

    private final CreateNewIndexService createNewIndexService;
    private final PollService pollService;

    private KendraClient kendra;

    public KendraService(IndexSearchService indexSearchService,
                         CreateNewIndexService createNewIndexService,
                         PollService pollService) {
        this.indexSearchService = indexSearchService;
        this.createNewIndexService = createNewIndexService;
        this.pollService = pollService;
    }
    @PostConstruct
    public void postConstruct(){
        AwsCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        kendra = KendraClient
                .builder()
                .region(Region.AP_SOUTH_1) // Change it with your region
                .credentialsProvider(() -> awsCredentials)
                .build();
    }

    public List<QueryResultItem> getQueryResult(String queryText, String indexId){
        return indexSearchService.searchIndex(queryText, indexId, kendra);
    }

    public IndexCreationResultItem createIndex(String indexName, String indexDescription) throws InterruptedException {
        return createNewIndexService.createIndexInKendra(indexName, indexDescription, kendra);
    }


    public IndexStatus checkIndex(String indexId) {
        return pollService.checkIfIndexIsCreated(kendra, indexId);
    }
}
