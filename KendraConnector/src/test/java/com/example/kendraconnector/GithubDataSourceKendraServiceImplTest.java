package com.example.kendraconnector;

import com.example.kendraconnector.dto.ResultItemDto;
import com.example.kendraconnector.exceptions.BasicKendraException;
import com.example.kendraconnector.service.KendraServices.GithubDataSourceKendraServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.kendra.KendraClient;
import software.amazon.awssdk.services.kendra.model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class GithubDataSourceKendraServiceImplTest {

    @Mock
    private KendraClient kendraClient;

    @InjectMocks
    private GithubDataSourceKendraServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckKendraIndexStatus() {
        String indexId = "testId";
        IndexStatus mockStatus = IndexStatus.ACTIVE;

        when(kendraClient.describeIndex(DescribeIndexRequest.builder().id(indexId).build()))
                .thenReturn(DescribeIndexResponse.builder().status(mockStatus).build());

        IndexStatus result = service.checkKendraIndexStatus(indexId);

        assertEquals(mockStatus, result);
    }

    // ... continue in similar fashion for the other methods ...

}
