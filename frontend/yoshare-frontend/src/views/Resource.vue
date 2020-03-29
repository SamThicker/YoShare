<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div class="all">
    <header-bar-Pro :switch_able="switch_able">
      <template v-slot:major>
        <member-resource-header-menu></member-resource-header-menu>
      </template>
    </header-bar-Pro>
    <div class="content">
      <router-view />
    </div>
  </div>
</template>

<script>
import MemberResourceHeaderMenu from "@/components/memberResource/MemberResourceHeaderMenu";
// import { mapState } from "vuex";
import HeaderBarPro from "../components/HeaderBarPro";

export default {
  name: "Resource",
  components: { HeaderBarPro, MemberResourceHeaderMenu },
  data() {
    return {
      switch_able: true
    };
  }
};
</script>

<style scoped>
.all {
  background-color: #f9f9f9;
  width: 100%;
  height: 100%;
}

.content {
  background-color: #f9f9f9;
  box-sizing: border-box;
  position: relative;
  width: 100%;
  height: calc(100% - 56px);
  text-align: center;
  display: block;
}
</style>
