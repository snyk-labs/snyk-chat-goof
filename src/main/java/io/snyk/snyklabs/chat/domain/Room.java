package io.snyk.snyklabs.chat.domain;

import io.snyk.snyklabs.chat.dto.SimpleRoomDto;
import io.snyk.snyklabs.user.User;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

public class Room {

    public final String name;
    public final String key;
    public final Set<User> users;

    public Room(String name) {
        this.name = name;
        this.key = generateKey(name);
        this.users = HashSet.empty();
    }

    private Room(String name, String key, Set<User> users) {
        this.name = name;
        this.key = key;
        this.users = users;
    }

    public Room subscribe(User user) {
        final Set<User> subscribedUsers = this.users.add(user);
        return new Room(this.name, this.key, subscribedUsers);
    }

    public Room unsubscribe(User user) {
        final Set<User> subscribedUsers = this.users.remove(user);
        return new Room(this.name, this.key, subscribedUsers);
    }

    public SimpleRoomDto asSimpleRoomDto() {
        return new SimpleRoomDto(this.name, this.key);
    }

    private String generateKey(String roomName) {
        return roomName.toLowerCase().trim().replaceAll("\\s+", "-");
    }
}
