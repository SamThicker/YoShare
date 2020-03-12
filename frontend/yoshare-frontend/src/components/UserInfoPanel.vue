<template>
  <div class="info-panel">
    <div class="sidebar">
      <UserInfoPanelSideBar></UserInfoPanelSideBar>
    </div>
    <div class="content"><router-view></router-view></div>
  </div>
</template>

<script>
import UserInfoPanelSideBar from "./UserInfoPanelSideBar";
export default {
  name: "UserInfoPanel",
  components: { UserInfoPanelSideBar }
};
</script>

<style scoped>
.sidebar {
  position: relative;
  width: 220px;
  float: left;
}

.content {
  position: relative;
  float: left;
  width: calc(100% - 220);
  min-width: 600px;
  overflow: hidden;
}
</style>
