package com.kojotdev.blog.multiroomchat.chat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NewRoomDto {

    public final String roomName;

    @JsonCreator
    public NewRoomDto(@JsonProperty("roomName") String roomName) {
        this.roomName = roomName;
    }
}
