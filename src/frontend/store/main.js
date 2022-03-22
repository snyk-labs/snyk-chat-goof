import Vue from "vue";

export const state = () => ({
  sidebar: true,
  username: "",
  roomList: [],
  usersInChatRoom: {},
  roomMessages: {}
});

export const getters = {
  sidebar: state => state.sidebar,
  username: state => state.username,
  roomList: state => state.roomList,
  roomName: state => roomId => {
    return state.roomList.find(room => room.key === roomId).name;
  },
  usersInChatRoom: state => roomId => {
    const userList = state.usersInChatRoom[roomId];
    return userList ? userList.users : [];
  },
  roomMessages: state => roomId => {
    const roomMessages = state.roomMessages[roomId];
    return roomMessages ? roomMessages.messages : [];
  }
};

export const mutations = {
  showSidebar(state, payload) {
    state.sidebar = payload;
  },
  setUsername(state, payload) {
    state.username = payload;
  },
  addRoom(state, room) {
    state.roomList.push(room);
  },
  initRoom(state, roomList) {
    state.roomList = roomList;
  },
  subscribe(state, roomKey) {
    subscribe(state, roomKey, true);
  },
  unsubscribe(state, roomKey) {
    subscribe(state, roomKey, false);
  },
  sendMessage(state, newMessage) {
    const roomMessage = state.roomMessages[newMessage.roomKey];
    if (roomMessage) {
      roomMessage.messages.push(newMessage.message);
    } else {
      const firstMessage = {
        roomKey: newMessage.roomKey,
        messages: [newMessage.message]
      };
      Vue.set(state.roomMessages, newMessage.roomKey, firstMessage);
    }
  },
  updateRoomUserList(state, roomUserList) {
    Vue.set(state.usersInChatRoom, roomUserList.roomKey, roomUserList);
  }
};

export const actions = {
  addRoom({ state, commit }, roomName) {
    if (this.stompClient && this.stompClient.connected) {
      const newRoom = { roomName: roomName };
      this.stompClient.send("/app/chat/addRoom", JSON.stringify(newRoom), {});
    }
  },
  subscribeRoomUserList({ state, commit }, roomId) {
    if (this.stompClient && this.stompClient.connected) {
      const subscribeUrl = "/chat/" + roomId + "/userList";
      this.stompClient.subscribe(
        subscribeUrl,
        tick => {
          const roomUserList = JSON.parse(tick.body);
          commit("updateRoomUserList", roomUserList);
        },
        { id: roomId + "_userList" }
      );
      this.stompClient.subscribe(
        "/chat/" + roomId + "/messages",
        tick => {
          const message = JSON.parse(tick.body);
          const roomMessage = { roomKey: roomId, message: message };
          commit("sendMessage", roomMessage);
        },
        { id: roomId + "_messages" }
      );
      const joinRoom = {
        roomKey: roomId,
        userName: state.username
      };
      this.stompClient.send(
        "/app/chat/" + roomId + "/join",
        JSON.stringify(joinRoom)
      );
      commit("subscribe", roomId);
    }
  },
  unsubscribe({ state, commit }, roomId) {
    const leaveRoom = {
      roomKey: roomId,
      userName: state.username
    };
    this.stompClient.send(
      "/app/chat/" + roomId + "/leave",
      JSON.stringify(leaveRoom)
    );
    this.stompClient.unsubscribe(roomId + "_userList");
    this.stompClient.unsubscribe(roomId + "_messages");
    commit("unsubscribe", roomId);
  },
  sendMessage({ state }, message) {
    message.message.userName = state.username;
    this.stompClient.send(
      "/app/chat/" + message.roomId + "/sendMessage",
      JSON.stringify(message.message)
    );
  }
};

function subscribe(state, roomKey, value) {
  state.roomList.map(room =>
    room.key === roomKey ? (room.subscribed = value) : room
  );
}
