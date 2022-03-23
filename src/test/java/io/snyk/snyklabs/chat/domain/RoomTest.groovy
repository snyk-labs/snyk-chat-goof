package io.snyk.snyklabs.chat.domain


import io.snyk.snyklabs.user.User
import spock.lang.Specification

class RoomTest extends Specification {

    def "new room should not have any user subscribed"() {
        given: "created new room"
        def newRoom = new Room("good name");
        expect: "user list should be empty"
        newRoom.users.isEmpty();
    }

    def "created new room should have a correct key"() {
        when: "creating a new room with random name"
        def newRoom = new Room(roomName);
        then: "the right key should be generated"
        newRoom.key == expectedKey;
        where:
        roomName                         | expectedKey
        "new room"                       | "new-room"
        "simple"                         | "simple"
        "WiTh UpperCase"                 | "with-uppercase"
        "  with   a   lot of  spaces   " | "with-a-lot-of-spaces"
        "with ^&*- chars"                | "with-^&*--chars"
    }

    def "room subscribe should be fine"() {
        given: "an empty room and user"
        def newRoom = new Room("good name");
        def user = new User("kojot");
        when: "user subscribe"
        def subscribedRoom = newRoom.subscribe(user);
        then: "user should be subscribed"
        !subscribedRoom.users.isEmpty();
        subscribedRoom.users.contains(user);
    }

    def "room unsubscribe should be fine"() {
        given: "a room with user"
        def user = new User("kojot");
        def newRoom = new Room("good name").subscribe(user)
        when: "user unsubscribe"
        def unsubscribedRoom = newRoom.unsubscribe(user);
        then: "room should be empty"
        unsubscribedRoom.users.isEmpty();
    }

    def "mapping to SimpleRoomDto should be correct"() {
        given: "create new room"
        def newRoom = new Room("good name");
        when: "map to SimpleRoomDto"
        def dto = newRoom.asSimpleRoomDto()
        then: "dto should be correct"
        dto.key == "good-name"
        dto.name == "good name"
    }
}
