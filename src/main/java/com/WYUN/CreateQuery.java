
package com.WYUN;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "owner", "limit" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateQuery {

    @JsonProperty("name")
    public String name;
    @JsonProperty("owner")
    public String owner;
    @JsonProperty("limit")
    public Integer limit;

    public CreateQuery withOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public CreateQuery withLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public CreateQuery withName(String name) {
        this.name = name;
        return this;
    }

}
