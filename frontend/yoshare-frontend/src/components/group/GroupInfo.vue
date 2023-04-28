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
  components: { GroupInfoPanelSideBar },
  mounted() {
    let pathParts = this.$route.path.split("/");
    if (pathParts[pathParts.length - 1] === "setting") {
      let path = this.$route.path + "/simple";
      this.$router.push(path);
    }
  },
  watch: {
    path: function(path) {
      let pathParts = path.split("/");
      if (pathParts[pathParts.length - 1] === "setting") {
        let pathTo = this.$route.path + "/simple";
        this.$router.push(pathTo);
      }
    },
    deep: true
  },
  computed: {
    path: function() {
      return this.$route.path;
    }
  }
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
