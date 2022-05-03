<template>
  <v-app>
    <v-navigation-drawer
      :value="sidebar"
      app
      mobile-breakpoint="600"
    >
      <new-room></new-room>
      <v-divider></v-divider>
      <room-list></room-list>
    </v-navigation-drawer>

    <v-app-bar
      app
      color="indigo"
      dark
    >
      <v-row justify="center">
        <v-toolbar-items>
          <v-btn
            text
            to="/"
            large
          >Home</v-btn>
          <v-btn
            text
            to="/chat"
            large
          >Chat</v-btn>
          <v-btn
            text
            to="/about"
            large
          >About</v-btn>
        </v-toolbar-items>
      </v-row>
    </v-app-bar>

    <v-main>
      <v-container
        fluid
        fill-height
      >
        <v-layout
          align-top
          justify-center
        >
          <nuxt />
        </v-layout>
      </v-container>
    </v-main>
    <v-footer
      color="indigo"
      app
    >
      <span class="white--text">&copy; 2022 Snyk - based off the excellent work of <a
          href="http://kojotdev.com"
          target="_blank"
          class="white--text"
        >kojotdev.com</a></span>
    </v-footer>
  </v-app>
</template>

<script>
import NewRoom from "@/components/room/NewRoom.vue";
import RoomList from "@/components/room/RoomList.vue";
export default {
  components: {
    NewRoom,
    RoomList
  },
  computed: {
    sidebar() {
      return this.$store.getters["main/sidebar"];
    }
  }
};
</script>
<style>
</style>
