package com.WYUN;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Query {
    @JsonProperty("query")
    public String query;
}