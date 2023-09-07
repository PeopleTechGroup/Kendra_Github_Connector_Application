package com.example.kendraconnector;

import com.example.kendraconnector.controller.S3ConnectorKendraController;
import com.example.kendraconnector.exceptions.KendraIndexCreationException;
import com.example.kendraconnector.exceptions.IndexNotFoundException;
import com.example.kendraconnector.model.IndexCreationResultItem;
import com.example.kendraconnector.service.KendraService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.services.kendra.model.IndexStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class S3ConnectorKendraControllerTest {

    @InjectMocks
    private S3ConnectorKendraController controller;

    @Mock
    private KendraService kendraService;

    @Test
    public void testCreateKendraIndex_Success() throws InterruptedException {
        IndexCreationResultItem mockResult = new IndexCreationResultItem();
        when(kendraService.createIndex(anyString(), anyString())).thenReturn(mockResult);

        ResponseEntity<IndexCreationResultItem> response = controller.createKendraIndex("testName", "testDescription");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResult, response.getBody());
    }

    @Test
    public void testCreateKendraIndex_Failure() throws InterruptedException {
        when(kendraService.createIndex(anyString(), anyString())).thenThrow(KendraIndexCreationException.class);

        ResponseEntity<IndexCreationResultItem> response = controller.createKendraIndex("testName", "testDescription");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCheckIfIndexIsCreated_Success() {
        IndexStatus mockStatus = IndexStatus.ACTIVE;
        when(kendraService.checkIndex(anyString())).thenReturn(mockStatus);

        ResponseEntity<IndexStatus> response = controller.checkIfIndexIsCreated("testIndexId");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockStatus, response.getBody());
    }

    @Test
    public void testCheckIfIndexIsCreated_NotFound() {
        when(kendraService.checkIndex(anyString())).thenThrow(IndexNotFoundException.class);

        ResponseEntity<IndexStatus> response = controller.checkIfIndexIsCreated("testIndexId");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
