<template>
  <div class="group-category-note-wrap">
    <resource-panel></resource-panel>
  </div>
</template>

<script>
import ResourcePanel from "@/components/ResourcePanel.vue";
export default {
  name: "Resource",
  components: { ResourcePanel }
};
</script>

<style scoped>
.group-category-note-wrap {
  width: 100%;
  height: 100%;
  position: relative;
}
</style>
