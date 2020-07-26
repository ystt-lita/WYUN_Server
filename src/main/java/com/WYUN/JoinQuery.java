package com.WYUN;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JoinQuery {
    @JsonProperty("name")
    public String name;
    public JoinQuery withName(String name){
        this.name=name;
        return this;
    }
}