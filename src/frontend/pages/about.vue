<template>
  <v-flex
    text-center
    xs12
    sm6
    md3
  >
    Hello about page

  </v-flex>

</template>

<script>
export default {
  fetch({ store }) {
    store.commit("main/showSidebar", false);
  },
  layout: "chat"
};
</script>

<style lang="scss" scoped>
</style>