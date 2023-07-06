package com.example.kendraconnector.controller;

import com.example.kendraconnector.model.IndexCreationResultItem;
import com.example.kendraconnector.service.KendraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.kendra.model.IndexStatus;

@RestController("api/v1")
public class S3ConnectorKendraController {

    @Autowired
    private  KendraService kendraService;

    @PostMapping("/index")
    public ResponseEntity<IndexCreationResultItem> createKendraIndex(@RequestHeader("indexName") String indexName, @RequestHeader("description") String description) throws InterruptedException {
        IndexCreationResultItem response = kendraService.createIndex(indexName, description);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/index")
    public ResponseEntity<IndexStatus> checkIfIndexIsCreated(@RequestHeader("indexId") String indexId) {
        IndexStatus indexStatus = kendraService.checkIndex(indexId);
        return ResponseEntity.ok(indexStatus);
    }
}

