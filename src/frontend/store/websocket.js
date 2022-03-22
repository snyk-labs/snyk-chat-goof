import SockJS from "sockjs-client";
import Stomp from "webstomp-client";

export const state = () => ({
  websocketUrl: process.env.baseUrl + "/ws",
  connected: false
});

export const getters = {
  connected: state => state.connected
};

export const mutations = {
  setConnected(state, status) {
    state.connected = status;
  }
};

export const actions = {
  connect({ state, commit }) {
    if (state.connected) return;
    this.socket = new SockJS(state.websocketUrl);
    this.stompClient = Stomp.over(this.socket);
    // comment the line below if you want to see debug messages
    this.stompClient.debug = msg => {};
    this.stompClient.connect(
      {},
      frame => {
        commit("setConnected", true);
        this.stompClient.subscribe("/app/chat/roomList", tick => {
          const roomList = JSON.parse(tick.body);
          commit("main/initRoom", roomList, { root: true });
        });
        // subscribe new rooms
        this.stompClient.subscribe("/chat/newRoom", tick => {
          const room = JSON.parse(tick.body);
          commit("main/addRoom", room, { root: true });
        });
      },
      error => {
        console.log(error);
        commit("setConnected", false);
      }
    );
  },
  subscribeRoomList() {}
};
