<template>
  <div>
    <v-list
      two-line
      subheader
    >
      <v-list-item
        v-for="(message, index) in messages"
        :key="index"
      >
        <template v-if="message.type == 'MESSAGE'">
          <v-list-item-avatar>
            <v-avatar color="#ff9b20">
              <span class="white--text headline">{{ message.userName.charAt(0) }}</span>
            </v-avatar>
          </v-list-item-avatar>
          <v-list-item-content>
            <v-list-item-title v-text="message.userName"></v-list-item-title>
            <v-list-item-subtitle v-html="marked(message.message)"></v-list-item-subtitle>
          </v-list-item-content>
        </template>
        <template v-else>
          <v-divider inset></v-divider>
          <v-subheader inset>{{ userEvent(message) }}</v-subheader>
          <v-divider inset></v-divider>
        </template>
      </v-list-item>
    </v-list>
  </div>
</template>

<script>
import marked from 'marked';

marked.setOptions({ sanitize: true });

export default {
  computed: {
    messages() {
      const roomId = this.$route.params.roomId;
      return this.$store.getters["main/roomMessages"](roomId);
    }
  },
  methods: {
    userEvent(message) {
      if (message.type == "JOIN") return message.userName + " joined!";
      if (message.type == "LEAVE") return message.userName + " left!";
      return "uknown message type";
    },
    marked: marked
  }
};
</script>

<style scoped>
</style>
