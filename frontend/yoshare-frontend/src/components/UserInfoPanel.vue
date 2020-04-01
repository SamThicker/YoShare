<template>
  <div class="info-panel-wrap">
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
  margin: 10px;
}

.content {
  position: relative;
  float: left;
  width: calc(100% - 220);
  min-width: 500px;
  overflow: hidden;
  margin: 10px;
  flex: 1;
  max-width: 700px;
}

.info-panel-wrap {
  padding: 20px;
  width: 100%;
  height: 100%;
  display: -webkit-box;
}
</style>
