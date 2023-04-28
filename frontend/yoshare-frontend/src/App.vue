<template>
  <div id="app">
    <!--        <div id="nav">-->
    <!--          <router-link to="/">Home</router-link> |-->
    <!--          <router-link to="/about">About</router-link> |-->
    <!--          <router-link to="/HeaderBar">HeaderBar</router-link> |-->
    <!--          <router-link to="/loginPage">LoginPage</router-link>-->
    <!--        </div>-->
    <router-view v-if="isRouterAlive" />
  </div>
</template>

<style lang="scss">
html,
body,
#app {
  height: 100%;
  margin: 0;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>
<script>
export default {
  name: "App",
  provide() {
    return {
      reload: this.reload
    };
  },
  data() {
    return {
      isRouterAlive: true
    };
  },
  methods: {
    reload() {
      this.isRouterAlive = false; //先关闭，
      this.$nextTick(function() {
        this.isRouterAlive = true; //再打开
      });
    }
  },
  components: {},
  mounted() {
    if (!this.$store.getters.token) return;
    this.$store.dispatch("GetInfoByToken");
  }
};
</script>
