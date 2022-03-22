<template>
  <v-layout justify-center>
    <room-chat v-if="isRoomSubscribed"></room-chat>
    <room-greeter
      v-else
      :roomKey="$route.params.roomId"
    ></room-greeter>

  </v-layout>
</template>

<script>
import RoomGreeter from "@/components/room/RoomGreeter.vue";
import RoomChat from "@/components/room/RoomChat.vue";
export default {
  layout: "chat",
  components: {
    RoomGreeter,
    RoomChat
  },
  computed: {
    isRoomSubscribed() {
      const roomId = this.$route.params.roomId;
      return this.$store.getters["main/roomList"].find(
        room => room.key === roomId
      ).subscribed;
    }
  }
};
</script>

<style lang="scss" scoped>
</style>