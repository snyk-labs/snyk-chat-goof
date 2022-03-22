package com.kojotdev.blog.multiroomchat.chat.controller;

import com.kojotdev.blog.multiroomchat.chat.domain.Room;
import com.kojotdev.blog.multiroomchat.chat.dto.ChatRoomUserListDto;
import com.kojotdev.blog.multiroomchat.chat.dto.NewRoomDto;
import com.kojotdev.blog.multiroomchat.chat.dto.SimpleRoomDto;
import com.kojotdev.blog.multiroomchat.chat.dto.UserRoomKeyDto;
import com.kojotdev.blog.multiroomchat.chat.service.RoomService;
import com.kojotdev.blog.multiroomchat.message.Message;
import com.kojotdev.blog.multiroomchat.message.MessageTypes;
import com.kojotdev.blog.multiroomchat.user.User;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import static java.lang.String.format;

@Controller
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private final RoomService roomService;
    private final SimpMessageSendingOperations messagingTemplate;

    public ChatController(RoomService roomService, SimpMessageSendingOperations messagingTemplate) {
        this.roomService = roomService;
        this.messagingTemplate = messagingTemplate;
    }

    @SubscribeMapping("/chat/roomList")
    public List<SimpleRoomDto> roomList() {
        return roomService.roomList();
    }

    @MessageMapping("/chat/addRoom")
    @SendTo("/chat/newRoom")
    public SimpleRoomDto addRoom(NewRoomDto newRoom) {
        return roomService.addRoom(newRoom.roomName);
    }

    @MessageMapping("/chat/{roomId}/join")
    public ChatRoomUserListDto userJoinRoom(UserRoomKeyDto userRoomKey, SimpMessageHeaderAccessor headerAccessor) {
//        with enabled spring security
//        final String securityUser = headerAccessor.getUser().getName();
        final String username = (String) headerAccessor.getSessionAttributes().put("username", userRoomKey.userName);
        final Message joinMessage = new Message(MessageTypes.JOIN, userRoomKey.userName, "");
        return roomService.addUserToRoom(userRoomKey)
                .map(userList -> {
                    messagingTemplate.convertAndSend(format("/chat/%s/userList", userList.roomKey), userList);
                    sendMessage(userRoomKey.roomKey, joinMessage);
                    return userList;
                })
                .getOrElseGet(appError -> {
                    log.error("invalid room id...");
                    return new ChatRoomUserListDto(userRoomKey.roomKey, HashSet.empty());
                });
    }

    @MessageMapping("/chat/{roomId}/leave")
    public ChatRoomUserListDto userLeaveRoom(UserRoomKeyDto userRoomKey, SimpMessageHeaderAccessor headerAccessor) {
        final Message leaveMessage = new Message(MessageTypes.LEAVE, userRoomKey.userName, "");
        return roomService.removeUserFromRoom(userRoomKey)
                .map(userList -> {
                    messagingTemplate.convertAndSend(format("/chat/%s/userList", userList.roomKey), userList);
                    sendMessage(userRoomKey.roomKey, leaveMessage);
                    return userList;
                })
                .getOrElseGet(appError -> {
                    log.error("invalid room id...");
                    return new ChatRoomUserListDto(userRoomKey.roomKey, HashSet.empty());
                });
    }

    @MessageMapping("chat/{roomId}/sendMessage")
    public Message sendMessage(@DestinationVariable String roomId, Message message) {
        messagingTemplate.convertAndSend(format("/chat/%s/messages", roomId), message);
        return message;
    }

    public void handleUserDisconnection(String userName) {
        final User user = new User(userName);
        final Message leaveMessage = new Message(MessageTypes.LEAVE, userName, "");
        List<Room> userRooms = roomService.disconnectUser(user);
        userRooms
                .map(room -> new ChatRoomUserListDto(room.key, room.users))
                .forEach(roomUserList -> {
                    messagingTemplate.convertAndSend(format("/chat/%s/userList", roomUserList.roomKey), roomUserList);
                    sendMessage(roomUserList.roomKey, leaveMessage);
                });
    }

}
