<template>
  <v-flex
    sm12
    md8
    text-center
  >
    <form @submit.prevent="sendMessage">
      <v-text-field
        counter="160"
        :rules="rules"
        label="Type a message"
        type="text"
        v-model="message"
      >
        <template v-slot:append>
          <v-btn
            color="primary"
            @click="sendMessage"
          >
            Send message
            <v-icon right>keyboard_return</v-icon>
          </v-btn>
        </template>
      </v-text-field>
    </form>
  </v-flex>
</template>

<script>
export default {
  data() {
    return {
      message: "",
      rules: [v => v.length <= 160 || 'Max 160 characters']
    };
  },
  computed: {
    roomId() {
      return this.$route.params.roomId;
    }
  },
  methods: {
    sendMessage() {
      const newMessage = {
        type: "MESSAGE",
        // userName: "kojot",
        message: (this.message.length > 160) ? this.message.substring(0,160) : this.message
      };
      const messageWithRoomId = { roomId: this.roomId, message: newMessage };
      this.$store.dispatch("main/sendMessage", messageWithRoomId);
      this.message = "";
    }
  }
};
</script>

<style scoped>
</style>
