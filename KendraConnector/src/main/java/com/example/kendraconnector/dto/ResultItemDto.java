package com.example.kendraconnector.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ResultItemDto {
    private String id;
    private String description;
}
