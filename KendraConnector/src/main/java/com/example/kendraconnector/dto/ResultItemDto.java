package com.example.kendraconnector.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ResultItemDto {
    private String id;
    private String description;

    public static class Builder {
        private String indexName;
        private String description;

        public Builder indexName(String indexName) {
            this.indexName = indexName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public ResultItemDto build() {
            return new ResultItemDto(indexName, description);
        }
    }
}
