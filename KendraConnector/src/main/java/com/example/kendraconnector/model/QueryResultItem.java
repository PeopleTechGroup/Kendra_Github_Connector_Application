package com.example.kendraconnector.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class QueryResultItem {
    private String id;
    private String type;
    private String format;
    private String additionalAttributes;
    private String documentId;
    private String documentTitle;
    private String documentExcerpt;
    private String documentURI;
    private String documentAttributes;
    private String scoreAttributes;
}
