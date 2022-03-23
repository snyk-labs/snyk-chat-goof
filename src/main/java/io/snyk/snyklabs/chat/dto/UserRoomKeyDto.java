package io.snyk.snyklabs.chat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRoomKeyDto {

    public final String roomKey;
    public final String userName;

    @JsonCreator
    public UserRoomKeyDto(@JsonProperty("roomKey") String roomKey,
                          @JsonProperty("userName") String userName) {
        this.roomKey = roomKey;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "UserRoomKeyDto{" +
                "roomKey='" + roomKey + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
