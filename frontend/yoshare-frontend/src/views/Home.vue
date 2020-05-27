<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div class="all">
    <div class="home">
      <header-bar-Pro :switch_able="switch_able">
        <template v-slot:major>
          <header-menu></header-menu>
        </template>
        <template v-slot:minor>
          <div style="height: 100%; background-color: #0081C6"></div>
        </template>
      </header-bar-Pro>
      <div class="content" @click="changeSwitchAble(!switch_able)">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
import HeaderMenu from "../components/HeaderMenu";
import { mapState } from "vuex";
import HeaderBarPro from "../components/HeaderBarPro";
export default {
  name: "Home",
  components: { HeaderBarPro, HeaderMenu },
  mounted() {
    if (this.$route.path === "/group" || !this.userInfo.id) return;
    this.$router.push("/group");
  },
  watch: {
    userInfo: function(val) {
      if (val.id) {
        this.$router.push("/group");
      }
    },
    path: function(val) {
      if (val === "/" && this.userInfo.id) {
        this.$router.push("/group");
      }
    }
  },
  data() {
    return {
      switch_able: false
    };
  },
  computed: {
    ...mapState({
      account: state => state.user.account
    }),
    userInfo: function() {
      return this.$store.state.user.info;
    },
    path: function() {
      return this.$route.path;
    }
  },
  methods: {
    changeSwitchAble(/*bool*/) {
      this.switch_able = false;
    }
  }
};
</script>

<style scoped>
.all {
  background-color: #f9f9f9;
}
.content {
  background-color: #f9f9f9;
  box-sizing: border-box;
  position: relative;
  width: 100%;
  height: 100%;
  text-align: center;
  display: block;
}
</style>
