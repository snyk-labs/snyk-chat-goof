<template>
  <v-flex
    xs12
    sm5
    text-center
  >
    <p>Welcome, <b>{{ username }}</b>! Select the room or add a new one to chat with other users.</p>
  </v-flex>

</template>

<script>
export default {
  fetch({ store }) {
    store.commit("main/showSidebar", true);
    store.dispatch("websocket/connect");
  },
  layout: "chat",
  computed: {
    username() {
      return this.$store.getters["main/username"];
    }
  }
};
</script>

<style scoped>
</style>