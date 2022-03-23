package io.snyk.snyklabs.chat.dto;

import io.snyk.snyklabs.user.User;
import io.vavr.collection.Set;

public class ChatRoomUserListDto {

    public final String roomKey;
    public final Set<User> users;

    public ChatRoomUserListDto(String roomKey, Set<User> users) {
        this.roomKey = roomKey;
        this.users = users;
    }
}
