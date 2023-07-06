package com.example.kendraconnector.service.KendraServices;

import com.example.kendraconnector.dto.ResultItemDto;
import com.example.kendraconnector.model.QueryResultItem;
import org.apache.commons.lang3.tuple.Pair;
import software.amazon.awssdk.services.kendra.model.IndexStatus;

import java.util.List;

public interface GithubDataSourceKendraService {
    public ResultItemDto createKendraIndex(String indexName, String indexDescription);

    public IndexStatus checkKendraIndexStatus(String indexId);

    public boolean checkKendraIndexExists(String indexId);

    public boolean checkKendraDataSourceExists(String indexId, String dataSourceId);

    public String createKendraGithubDataSource(String indexId);

    public List<QueryResultItem> getQueryResult(String query, String indexId);

    public List<Pair<String,String>> getAllKendraIndexes();

    public List<Pair<String,String>> getAllDataSources(String indexId);

    public String createDataSourceConfiguration();

}
