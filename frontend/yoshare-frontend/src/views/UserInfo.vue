<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div class="user-info">
    <header-bar-Pro id="header-bar" :switch_able="switch_able">
      <template v-slot:major>
        <header-menu></header-menu>
      </template>
    </header-bar-Pro>
    <UserInfoPanel class="info-panel"></UserInfoPanel>
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
html,
body,
#app {
  height: 100%;
}

#header-bar {
  height: 56px;
  box-sizing: border-box;
}

.user-info {
  width: 100%;
  height: 100%;
}

.info-panel {
  position: relative;
  top: 0;
  height: 700px;
  width: 100%;
  margin-right: auto;
  margin-left: auto;
  padding-left: 15px;
  padding-right: 15px;
}

.content {
  position: relative;
  top: 5px;
  width: 100%;
  height: 2000px;
  font-size: 300px;
  text-align: center;
}
</style>
