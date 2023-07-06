package com.example.kendraconnector.service;
import com.example.kendraconnector.model.QueryResultItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.kendra.KendraClient;
import software.amazon.awssdk.services.kendra.model.QueryRequest;
import software.amazon.awssdk.services.kendra.model.QueryResponse;

import java.util.List;

@Slf4j
@Service
public class IndexSearchService {
    public List<QueryResultItem> searchIndex(String queryText, String indexId, KendraClient kendra){

        QueryRequest queryRequest = QueryRequest
                .builder()
                .queryText(queryText)
                .indexId(indexId)
                .build();

        QueryResponse queryResponse = kendra.query(queryRequest);
        List<software.amazon.awssdk.services.kendra.model.QueryResultItem> queryQueryResultItems = queryResponse.resultItems();
        List<QueryResultItem> resultItems = queryQueryResultItems.stream()
                .map(queryResultItem -> {
                    QueryResultItem model = new QueryResultItem();
                    model.setId(queryResultItem.id());
                    model.setType(queryResultItem.typeAsString());
                    model.setFormat(queryResultItem.formatAsString());
                    model.setDocumentTitle(queryResultItem.documentTitle().text());
                    model.setDocumentExcerpt(queryResultItem.documentExcerpt().text());
                    model.setDocumentId(queryResultItem.documentId());
                    model.setDocumentURI(queryResultItem.documentURI());
                    model.setScoreAttributes(queryResultItem.scoreAttributes().scoreConfidenceAsString());
                    return model;
                })
                .toList();
        log.info("\nThere are {} results for queryText: {}",resultItems.size(), queryText);
        return resultItems;
        }
    }


