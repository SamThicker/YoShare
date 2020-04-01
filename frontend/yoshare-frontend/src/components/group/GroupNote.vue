<template>
  <div class="group-category-note-wrap">
    <resource-panel></resource-panel>
    <group-note-reading>
      <div class="note-content" v-loading="noteLoading">
        <router-view/>
      </div>
    </group-note-reading>
  </div>
</template>

<script>
import ResourcePanel from "@/components/ResourcePanel";
import GroupNoteReading from "./GroupNoteReading";
export default {
  name: "Note",
  components: { GroupNoteReading, ResourcePanel }
};
</script>

<style scoped>
.group-category-note-wrap {
  width: 100%;
  height: 100%;
  position: relative;
}
</style>
