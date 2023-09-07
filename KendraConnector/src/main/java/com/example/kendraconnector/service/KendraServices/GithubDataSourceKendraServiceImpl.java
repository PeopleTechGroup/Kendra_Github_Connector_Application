package com.example.kendraconnector.service.KendraServices;

import com.example.kendraconnector.dto.ResultItemDto;
import com.example.kendraconnector.exceptions.BasicKendraException;
import com.example.kendraconnector.exceptions.DataSourceCreationException;
import com.example.kendraconnector.exceptions.KendraIndexCreationException;
import com.example.kendraconnector.exceptions.KendraQueryException;
import com.example.kendraconnector.model.QueryResultItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.kendra.KendraClient;
import software.amazon.awssdk.services.kendra.model.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class GithubDataSourceKendraServiceImpl implements GithubDataSourceKendraService {

    @Autowired  private KendraClient kendraClient;
    @Value("${aws.iamRole.arn}")
    private String indexRoleArn;

    @Value("${aws.githubProfile.secret.arn}")
    private String secretArn;

    @Value("${aws.githubProfile.iamRole.arn}")
    private String iamRole;

    @Value("${aws.githubProfile.hostUrl}")
    private String hostUrl;

    @Value("${aws.githubProfile.organizationName}")
    private String organizationName;



    @Override
    public ResultItemDto createKendraIndex(String indexName, String indexDescription) {
        try {
            CreateIndexRequest indexRequest = CreateIndexRequest
                    .builder()
                    .edition(IndexEdition.DEVELOPER_EDITION)
                    .description(indexDescription)
                    .name(indexName)
                    .roleArn(indexRoleArn)
                    .build();
            CreateIndexResponse createIndexResponse = kendraClient.createIndex(indexRequest);
            log.debug("Index with id: "+createIndexResponse.id()+" will be created");
            return ResultItemDto.builder().id(createIndexResponse.id()).description(indexDescription).build();
        } catch (Exception e) {
            throw new KendraIndexCreationException("Exception thrown when creating a Kendra Index. " + e.getMessage());
        }
    }

    @Override
    public IndexStatus checkKendraIndexStatus(String indexId) {
        try {
            DescribeIndexRequest describeIndexRequest = DescribeIndexRequest.builder().id(indexId).build();
            DescribeIndexResponse describeIndexResponse = kendraClient.describeIndex(describeIndexRequest);
            return describeIndexResponse.status();
        } catch (Exception e) {
            throw new BasicKendraException("Exception thrown when checking for Kendra index status. "+ e.getMessage());
        }
    }

    @Override
    public boolean checkKendraIndexExists(String indexId) {
        try {
            DescribeIndexRequest describeIndexRequest = DescribeIndexRequest.builder().id(indexId).build();
            DescribeIndexResponse describeIndexResponse = kendraClient.describeIndex(describeIndexRequest);
            IndexStatus status = describeIndexResponse.status();

            if (status == IndexStatus.CREATING || status == IndexStatus.UPDATING || status == IndexStatus.DELETING) {
                do {
                    TimeUnit.SECONDS.sleep(60);
                    status = describeIndexResponse.status();
                } while (status == IndexStatus.CREATING);
            }

            return status != IndexStatus.FAILED;
        } catch (Exception e) {
            throw new KendraIndexCreationException(String.format("Exception thrown when checking Kendra index %s status. %s" , indexId, e.getMessage()));
        }
    }

    @Override
    public String createKendraGithubDataSource(String indexId) {
        try {
            CreateDataSourceRequest createDataSourceRequest = CreateDataSourceRequest
                    .builder()
                    .indexId(indexId)
                    .name("Github_Datasource")
                    .description("Github_Datasource_Description")
                    .roleArn(iamRole)
                    .type(DataSourceType.GITHUB)
                    .configuration(DataSourceConfiguration
                            .builder()
                            .gitHubConfiguration(createGithubConfiguration()).build())
                    .build();
            return kendraClient.createDataSource(createDataSourceRequest).id();
        } catch(Exception e) {
            throw new DataSourceCreationException(String.format("Exception thrown when trying to create a Github datasource for the given Kendra Index %s %s", indexId, e.getMessage()));
        }
    }

    @Override
    public List<QueryResultItem> getQueryResult(String query, String indexId) {
        QueryRequest queryRequest = QueryRequest
                .builder()
                .queryText(query)
                .indexId(indexId)
                .build();

        try {
            QueryResponse queryResponse = kendraClient.query(queryRequest);
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
            log.info("\nThere are {} results for query: {}", resultItems.size(), query);
            return resultItems;
        } catch (Exception e) {
            throw new KendraQueryException(String.format("Exception thrown when searching a query %s through the given Kendra index %s %s", query, indexId, e.getMessage()));
        }
    }

    @Override
    public boolean  checkKendraDataSourceExists(String indexId, String dataSourceId) {

        DescribeDataSourceRequest describeDataSourceRequest = DescribeDataSourceRequest
                .builder()
                .indexId(indexId)
                .id(dataSourceId)
                .build();

        try {
            DescribeDataSourceResponse describeDataSourceResponse = kendraClient.describeDataSource(describeDataSourceRequest);
            DataSourceStatus status = describeDataSourceResponse.status();

            if(status == DataSourceStatus.CREATING || status == DataSourceStatus.UPDATING || status == DataSourceStatus.DELETING) {
                do {
                    TimeUnit.SECONDS.sleep(60);
                    status = describeDataSourceResponse.status();
                } while (status == DataSourceStatus.CREATING);
            }

            return status != DataSourceStatus.FAILED;
        } catch (Exception e) {
            throw new DataSourceCreationException(String.format("Exception thrown when retrieving the Datasource %s for the given index %s ", dataSourceId, indexId));
        }
    }

    @Override
    public List<Pair<String,String>> getAllDataSources(String indexId) {
        List<Pair<String,String>> dataSourceTypes = new ArrayList<>();
        ListDataSourcesRequest listDataSourcesRequest = ListDataSourcesRequest.builder().indexId(indexId).build();
        ListDataSourcesResponse listDataSourcesResponse = kendraClient.listDataSources(listDataSourcesRequest);
        List<DataSourceSummary> kendraDataSources = listDataSourcesResponse.summaryItems();
        for (DataSourceSummary dataSource : kendraDataSources) {
            dataSourceTypes.add(new ImmutablePair<>(dataSource.id(),dataSource.type().toString()));
        }
        return dataSourceTypes;
    }

    // @Override
    private SaaSConfiguration createSaasConfiguration() {
        return SaaSConfiguration.builder()
                .organizationName(organizationName)
                .hostUrl(hostUrl)
                .build();
    }

    private GitHubConfiguration createGithubConfiguration() {
        return GitHubConfiguration.builder()
                .saaSConfiguration(createSaasConfiguration())
                .secretArn(secretArn)
                .build();
    }

    public String createDataSourceConfiguration() {
        DataSourceConfiguration dataSourceConfiguration = DataSourceConfiguration
                .builder()
                .gitHubConfiguration(createGithubConfiguration()).build();
        return dataSourceConfiguration.toString();
    }

    @Override
    public List<Pair<String,String>> getAllKendraIndexes() {

        List<Pair<String,String>> indexInfoPairs = new ArrayList<>();
        // Create a ListIndicesRequest
        ListIndicesRequest listIndicesRequest = ListIndicesRequest.builder().build();

        // Call the ListIndices API
        ListIndicesResponse listIndicesResponse = kendraClient.listIndices(listIndicesRequest);

        // Extract the index details from the response
        List<IndexConfigurationSummary> indexes = listIndicesResponse.indexConfigurationSummaryItems();
        for (IndexConfigurationSummary index : indexes) {
            String indexId = index.id();
            String indexName = index.name();
            indexInfoPairs.add(new ImmutablePair<>(indexId,indexName));
        }

        return indexInfoPairs;
    }

}
