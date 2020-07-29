package com.WYUN;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "to","body"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class TellQuery {
    @JsonProperty("to")
    public String to;
    @JsonProperty("body")
    public String body;

    public TellQuery withDest(String name){
        this.to=name;
        return this;
    }
    public TellQuery withBody(String body){
        this.body=body;
        return this;
    }
}