package io.snyk.snyklabs.chat.service


import io.snyk.snyklabs.chat.dto.UserRoomKeyDto
import io.snyk.snyklabs.user.User
import spock.lang.Specification

class RoomServiceTest extends Specification {

    def "created room service should have one default main room"() {
        given: "new room service"
        def roomService = new RoomService();
        when: "get the room list"
        def roomList = roomService.roomList();
        then: "got one default main room"
        roomList.size() == 1;
        roomList.get().key == "main-room";
        !roomList.get().subscribed;
    }

    def "adding new room should work correct"() {
        given: "new room service"
        def roomService = new RoomService();
        when: "adding new room with it's name"
        def room = roomService.addRoom("My new room")
        then: "new room should be created and added to room list"
        room.key == "my-new-room"
        roomService.roomList().size() == 2
        roomService.roomList().contains(room)
    }

    def "new added room should have empty user list"() {
        given: "new room service"
        def roomService = new RoomService();
        when: "taking user list from default room"
        def userList = roomService.usersInChatRoom("main-room")
        then: "user list should be empty and the key should be correct"
        userList.isRight()
        def chatRoomUserList = userList.get()
        chatRoomUserList.users.isEmpty()
        chatRoomUserList.roomKey == "main-room"
    }

    def "adding users to room should work correct"() {
        given: "new room service and users"
        def roomService = new RoomService();
        def user = new UserRoomKeyDto("main-room", "kojot")
        when: "adding user to default room"
        def userList = roomService.addUserToRoom(user);
        then: "user list should have one user"
        userList.isRight();
        def chatRoomUserList = userList.get()
        chatRoomUserList.roomKey == "main-room"
        !chatRoomUserList.users.isEmpty()
        chatRoomUserList.users.contains(new User("kojot"))
    }

    def "removing user from room should work correct"() {
        given: "room service with user"
        def roomService = new RoomService();
        def user = new UserRoomKeyDto("main-room", "kojot")
        roomService.addUserToRoom(user);
        when: "adding user to default room"
        def userList = roomService.removeUserFromRoom(user)
        then: "user list should have no user"
        userList.isRight();
        def chatRoomUserList = userList.get()
        chatRoomUserList.roomKey == "main-room"
        chatRoomUserList.users.isEmpty()
    }
}
