<template>
  <v-container fluid>
    <div class="parent">
      <div class="users">
        <v-navigation-drawer
          v-bind:class="[userList ? 'showUserList' : 'hideUserList']"
          mobile-breakpoint="300"
        >
          <v-list
            dense
            nav
          >
            <v-list-item
              v-for="user in usersInChatRoom"
              :key="user.name"
            >
              <v-list-item-avatar>
                <v-avatar color="#ff9b20">
                  <span class="white--text headline">{{ user.name.charAt(0) }}</span>
                </v-avatar>
              </v-list-item-avatar>
              <v-list-item-content>
                <v-list-item-title>{{ user.name }}</v-list-item-title>
              </v-list-item-content>
            </v-list-item>
          </v-list>
        </v-navigation-drawer>
      </div>
      <div class="chat-container">
        <div class="header">
          <v-card
            color="grey lighten-4"
            flat
            tile
            class="flex-grow-1"
          >
            <v-toolbar dense>
              <v-app-bar-nav-icon @click="userList = !userList"></v-app-bar-nav-icon>
              <v-toolbar-title>{{ roomName }} conversation</v-toolbar-title>
              <div class="flex-grow-1"></div>
              <v-btn
                color="error"
                @click="unsubscribe"
              >Leave this room...</v-btn>
            </v-toolbar>
          </v-card>
        </div>
        <div class="footer">
          <chat-message-sender></chat-message-sender>
        </div>
        <div class="scroll">
          <chat-content></chat-content>
        </div>
      </div>
    </div>
  </v-container>
</template>

<script>
import ChatContent from "@/components/room/ChatContent.vue";
import ChatMessageSender from "@/components/room/ChatMessageSender.vue";
export default {
  components: {
    ChatContent,
    ChatMessageSender
  },
  data() {
    return {
      userList: true
    };
  },
  computed: {
    roomName() {
      return this.$store.getters["main/roomName"](this.roomId);
    },
    roomId() {
      return this.$route.params.roomId;
    },
    usersInChatRoom() {
      return this.$store.getters["main/usersInChatRoom"](this.roomId);
    }
  },
  methods: {
    unsubscribe() {
      const roomId = this.$route.params.roomId;
      this.$store.dispatch("main/unsubscribe", roomId);
    }
  }
};
</script>

<style scoped>
.parent {
  background-color: #0fd6ea;
  height: 100%;
  display: flex;
  flex-flow: row wrap;
}

.users {
  margin: 5px;
}
.users .showUserList {
  width: 200px !important;
}

.users .hideUserList {
  width: 0px !important;
}

.chat-container {
  margin: 5px;
  display: flex;
  flex-flow: column nowrap;
  /* flex-direction: column; */
  flex-grow: 1;
}

.header {
  height: 60px;
}

.footer {
  display: flex;
  flex-flow: row wrap;
  justify-content: center;
}

.content {
  background-color: rgb(255, 255, 255);
  display: flex;
  flex-basis: 100%;
  flex-flow: column nowrap;
  justify-content: flex-end;
}

div.scroll {
  background-color: rgb(255, 255, 255);
  height: 530px;
  overflow-x: hidden;
  overflow-y: auto;
  text-align: center;
  display: flex;
  flex-direction: column-reverse;
}
</style>
