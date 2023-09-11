package com.example.kendraconnector.controller;

import com.example.kendraconnector.exceptions.KendraIndexCreationException;
import com.example.kendraconnector.exceptions.IndexNotFoundException;
import com.example.kendraconnector.model.IndexCreationResultItem;
import com.example.kendraconnector.service.KendraService;

import software.amazon.awssdk.services.kendra.model.IndexStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("api/v1")
public class S3ConnectorKendraController {

    @Autowired
    private KendraService kendraService;

    @PostMapping("/index")
    public ResponseEntity<IndexCreationResultItem> createKendraIndex(@RequestHeader("indexName") String indexName, @RequestHeader("description") String description) throws InterruptedException {
        try {
            IndexCreationResultItem response = kendraService.createIndex(indexName, description);
            return ResponseEntity.ok(response);
        } catch (KendraIndexCreationException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/index")
    public ResponseEntity<IndexStatus> checkIfIndexIsCreated(@RequestHeader("indexId") String indexId) {
        try {
            IndexStatus indexStatus = kendraService.checkIndex(indexId);
            return ResponseEntity.ok(indexStatus);
        } catch (IndexNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler({KendraIndexCreationException.class, IndexNotFoundException.class})
    public ResponseEntity<String> handleExceptions(Exception e) {
        if (e instanceof KendraIndexCreationException) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (e instanceof IndexNotFoundException) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

