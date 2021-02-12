package com.WYUN;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "members" })
public class Members {
    @JsonProperty("members")
    public List<Member> members;

    public Members() {
        members = new ArrayList<>();
    }

    public Members withElement(Member element) {
        this.members.add(element);
        return this;
    }
}
