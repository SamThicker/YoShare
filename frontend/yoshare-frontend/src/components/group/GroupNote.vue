<template>
  <div class="group-resource-note-wrap">

    <group-resource-panel class="resource-panel"
      :type="'NOTE'"
      :previewItemClickCallback="noteItemClicked"
      :itemStarCallback="itemStarClicked"
      :itemUnstarCallback="itemUnstarClicked"
      :itemShareCallback="itemShareClicked"
      :classificationsCallback="classificationsCallBack"
      :refresh="refresh"
    ></group-resource-panel>
    <div class="note-content" v-loading="noteLoading">
      <router-view/>
    </div>
  </div>
</template>

<script>
import GroupResourcePanel from "./GroupResourcePanel";
export default {
  name: "GroupNote",
  components: { GroupResourcePanel },
  data() {
    return {
      noteLoading: false
    };
  }
};
</script>

<style scoped>
.group-resource-note-wrap {
  width: 100%;
  height: 100%;
  position: relative;
  display: -webkit-box;
  -webkit-box-orient: horizontal;
}

.resource-panel {
  width: 500px;
  height: 100%;
}

.note-content {
  height: 100%;
  flex: 1;
  width: 100%;
  border: 1px solid #eee;
  box-sizing: border-box;
}
</style>
