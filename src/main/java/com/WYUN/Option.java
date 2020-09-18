
package com.WYUN;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "owner",
    "limit"
})
public class Option {

    @JsonProperty("owner")
    public String owner;
    @JsonProperty("limit")
    public Integer limit;

    public Option withOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public Option withLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

}
