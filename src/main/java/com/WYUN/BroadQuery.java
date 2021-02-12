package com.WYUN;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "body" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class BroadQuery {
    @JsonProperty("body")
    public String body;

    public BroadQuery withBody(String body) {
        this.body = body;
        return this;
    }
}