<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div class="user-info">
    <header-bar-Pro id="header-bar" :switch_able="switch_able">
      <template v-slot:major>
        <header-menu></header-menu>
      </template>
    </header-bar-Pro>
    <div class="user-info-wrap">
      <UserInfoPanel class="info-panel"></UserInfoPanel>
    </div>
  </div>
</template>

<script>
import HeaderMenu from "../components/HeaderMenu";
import { mapState } from "vuex";
import HeaderBarPro from "../components/HeaderBarPro";
import UserInfoPanel from "../components/UserInfoPanel";
export default {
  name: "UserInfo",
  components: { UserInfoPanel, HeaderBarPro, HeaderMenu },
  data() {
    return {
      switch_able: false
    };
  },
  computed: {
    ...mapState({
      account: state => state.user.account
    })
  },
  methods: {
    changeSwitchAble(bool) {
      this.switch_able = bool;
    }
  }
};
</script>

<style scoped>
.user-info {
  position: relative;
  width: 100%;
  height: 100%;
  display: -webkit-box;
  -webkit-box-orient: vertical;
}

#header-bar {
  height: 56px;
  box-sizing: border-box;
}

.user-info-wrap {
  width: 100%;
  box-sizing: border-box;
  flex: 1;
  height: 100%;
}

.info-panel {
  position: relative;
  box-sizing: border-box;
  /*flex: 1;*/
  height: 100%;
  width: 100%;
}
</style>
