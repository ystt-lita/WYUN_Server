
package com.WYUN;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "option"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateQuery {

    @JsonProperty("name")
    public String name;
    @JsonProperty("option")
    public Option option;

    public CreateQuery withName(String name){
        this.name=name;
        return this;
    }
    public CreateQuery withOption(Option option) {
        this.option = option;
        return this;
    }

}
