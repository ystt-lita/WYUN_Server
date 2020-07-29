package com.WYUN;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "body"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class BroadQuery {
    @JsonProperty("body")
    public String body;

    public BroadQuery withBody(String body){
        this.body=body;
        return this;
    }
}