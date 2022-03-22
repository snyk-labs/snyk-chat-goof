<template>
  <v-flex
    xs12
    sm3
    text-center
  >
    Welcome in the <strong>{{ roomName }}</strong> room.
    <v-flex mt-5>
      <v-btn
        color="error"
        @click="subscribe"
      >Start Chatting</v-btn>
    </v-flex>
  </v-flex>
</template>

<script>
export default {
  props: {
    roomKey: {
      type: String,
      default: ""
    }
  },
  computed: {
    roomName() {
      return this.$store.getters["main/roomName"](this.roomKey);
    }
  },
  methods: {
    subscribe() {
      this.$store.dispatch("main/subscribeRoomUserList", this.roomKey);
    }
  }
};
</script>

<style lang="scss" scoped>
</style>