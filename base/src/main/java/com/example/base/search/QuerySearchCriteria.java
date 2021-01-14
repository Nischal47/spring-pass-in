package com.example.base.search;

import lombok.Data;

@Data
public class QuerySearchCriteria {

    private final String key;

    private final String operation;

    private final Object value;

    public QuerySearchCriteria(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }
}
