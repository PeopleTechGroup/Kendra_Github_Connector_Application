package com.example.kendraconnector.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.kendra.KendraClient;
import software.amazon.awssdk.services.kendra.model.DescribeIndexRequest;
import software.amazon.awssdk.services.kendra.model.DescribeIndexResponse;
import software.amazon.awssdk.services.kendra.model.IndexStatus;
@Service
public class PollService {
    public IndexStatus checkIfIndexIsCreated(KendraClient kendra, String indexId) {
        DescribeIndexRequest describeIndexRequest = DescribeIndexRequest.builder().id(indexId).build();
        DescribeIndexResponse describeIndexResponse = kendra.describeIndex(describeIndexRequest);
        return describeIndexResponse.status();
    }
}
