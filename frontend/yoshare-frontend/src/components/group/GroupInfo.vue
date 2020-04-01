<template>
  <div class="info-panel-wrap">
    <div class="sidebar">
      <GroupInfoPanelSideBar></GroupInfoPanelSideBar>
    </div>
    <router-view />
  </div>
</template>

<script>
import GroupInfoPanelSideBar from "@/components/group/GroupInfoPanelSideBar";

export default {
  name: "GroupInfo",
  components: { GroupInfoPanelSideBar }
};
</script>

<style scoped>
.info-panel-wrap {
  padding: 20px;
  width: 100%;
  height: 100%;
  display: -webkit-box;
  box-sizing: border-box;
}

.sidebar {
  position: relative;
  width: 220px;
  float: left;
  margin: 10px;
}
</style>
