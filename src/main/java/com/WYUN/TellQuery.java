package com.WYUN;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "to", "body" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class TellQuery {
    @JsonProperty("to")
    public String dest;
    @JsonProperty("body")
    public String body;

    public TellQuery withDest(String name){
        this.dest=name;
        return this;
    }
    public TellQuery withBody(String body){
        this.body=body;
        return this;
    }
}