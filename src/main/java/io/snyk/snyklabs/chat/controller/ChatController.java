package io.snyk.snyklabs.chat.controller;

import io.snyk.snyklabs.chat.domain.Room;
import io.snyk.snyklabs.chat.dto.ChatRoomUserListDto;
import io.snyk.snyklabs.chat.dto.NewRoomDto;
import io.snyk.snyklabs.chat.dto.SimpleRoomDto;
import io.snyk.snyklabs.chat.dto.UserRoomKeyDto;
import io.snyk.snyklabs.chat.service.RoomService;
import io.snyk.snyklabs.message.Message;
import io.snyk.snyklabs.message.MessageTypes;
import io.snyk.snyklabs.user.User;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

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

    @GetMapping("/hello")
    public void hello(@RequestParam String user, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().write("<h1>Hello: " + user + "</h1>");
        response.getWriter().flush();
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
