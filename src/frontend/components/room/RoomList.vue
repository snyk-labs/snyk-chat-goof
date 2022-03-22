<template>
  <v-list>
    <v-subheader>Rooms</v-subheader>
    <v-list-item
      v-for="(room, index) in roomList"
      :key="room.key"
      :to="'/chat/' + room.key"
    >
      <v-list-item-action>
        #{{ index + 1 }}
      </v-list-item-action>
      <v-list-item-content>
        <v-list-item-title :class="{ subscribed: room.subscribed }">{{ room.name }}</v-list-item-title>
      </v-list-item-content>
    </v-list-item>
  </v-list>
</template>

<script>
export default {
  computed: {
    roomList() {
      return this.$store.getters["main/roomList"];
    }
  }
};
</script>

<style scoped>
.v-list-item__title.subscribed {
  font-size: 1.1rem;
  font-weight: 500;
}
</style>