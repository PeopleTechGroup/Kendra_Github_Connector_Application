package com.example.kendraconnector.service;

import com.example.kendraconnector.model.IndexCreationResultItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.kendra.KendraClient;
import software.amazon.awssdk.services.kendra.model.*;

@Slf4j
@Service
public class CreateNewIndexService {
    @Value("${aws.iamRole.arn}")
    private String indexRoleArn;

    public IndexCreationResultItem createIndexInKendra(String indexName, String indexDescription, KendraClient kendra) throws InterruptedException {
        CreateIndexRequest indexRequest = CreateIndexRequest
                .builder()
                .edition(IndexEdition.DEVELOPER_EDITION)
                .description(indexDescription)
                .name(indexName)
                .roleArn(indexRoleArn)
                .build();
        CreateIndexResponse createIndexResponse = kendra.createIndex(indexRequest);
        IndexCreationResultItem indexCreationResultItem = new IndexCreationResultItem();
        indexCreationResultItem.setId(createIndexResponse.id());
        indexCreationResultItem.setDescription(indexDescription);
        log.debug("Index with id: "+createIndexResponse.id()+" will be created");
        return indexCreationResultItem;
    }
}

