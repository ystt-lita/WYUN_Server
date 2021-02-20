/***************************************************
 * Copyright 2020, 2021 EwdErna
 * LICENSE: ../../../../../LICENSE
 * This file is part of WYUN Server.
 * 
 *     WYUN Server is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     WYUN Server is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with WYUN Server.  If not, see <https://www.gnu.org/licenses/>.
 ***************************************************/
package com.WYUN;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name" })
public class Member {
    @JsonProperty("name")
    public String name;

    public Member withName(String name) {
        this.name = name;
        return this;
    }
}
