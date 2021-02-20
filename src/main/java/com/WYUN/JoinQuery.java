/***************************************************
 * Copyright 2020, 2021 EwdErna
 * LICENSE: ../../../../../LICENSE
 ***************************************************/
package com.WYUN;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JoinQuery {
    @JsonProperty("name")
    public String name;

    public JoinQuery withName(String name) {
        this.name = name;
        return this;
    }
}