
package com.WYUN;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "option"
})
public class RoomOption {

    @JsonProperty("option")
    public Option option;

    public RoomOption withOption(Option option) {
        this.option = option;
        return this;
    }

}
