<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div class="all">
    <div class="home">
      <header-bar-Pro :switch_able="switch_able">
        <template v-slot:major>
          <header-menu></header-menu>
        </template>
      </header-bar-Pro>
      <div class="content" @click="changeSwitchAble(!switch_able)">
        HELLO WORLD
      </div>
    </div>
  </div>
</template>

<script>
import HeaderMenu from "../components/HeaderMenu";
import { mapState } from "vuex";
import HeaderBarPro from "../components/HeaderBarPro";
export default {
  name: "UserHome",
  components: { HeaderBarPro, HeaderMenu },
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
.all {
  background-color: #fff;
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
