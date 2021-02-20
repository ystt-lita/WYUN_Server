/***************************************************
 * Copyright 2020, 2021 EwdErna
 * LICENSE: ../../../../../LICENSE
 ***************************************************/
package com.WYUN;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "rooms" })
public class RoomList {
    @JsonProperty("rooms")
    public List<CreateQuery> rooms;

    public RoomList() {
        rooms = new ArrayList<>();
    }

    public RoomList withElement(CreateQuery element) {
        this.rooms.add(element);
        return this;
    }
}