<template>
  <div class="group-setting-wrap">
    <group-info class="group-info-wrap"></group-info>
  </div>
</template>

<script>
import GroupInfo from "./GroupInfo";
export default {
  name: "GroupSetting",
  components: { GroupInfo }
};
</script>

<style scoped>
.group-setting-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
}

.group-info-wrap {
  width: 100%;
  height: 100%;
  position: relative;
}
</style>
