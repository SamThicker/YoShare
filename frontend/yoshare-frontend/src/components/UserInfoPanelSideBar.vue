<template>
  <ul>
    <li
      class="setting-item"
      v-for="(setting, index) in settings"
      v-bind:key="setting.name"
      :class="active === index ? 'active' : ''"
      @click="activeEl(index)"
    >
      <router-link :to="setting.link">
        <a>{{ setting.name }}</a>
      </router-link>
    </li>
  </ul>
</template>

<script>
export default {
  name: "UserInfoPanelSideBar",
  data() {
    return {
      settings: [
        { name: "基本信息", icon: "", link: "/userInfo/simpleInfo" },
        { name: "安全信息", icon: "", link: "/userInfo/securityInfo" }
      ],
      active: 0
    };
  },
  methods: {
    activeEl(index) {
      this.active = index;
    }
  }
};
</script>

<style scoped>
a {
  text-decoration: none;
  color: #333;
}

ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
}

.setting-item {
  width: 100%;
  height: 50px;
  cursor: pointer;
  line-height: 50px;
  box-sizing: border-box;
  border: 1px solid transparent;
}

.setting-item a {
  padding: 10px;
}

.active {
  background-color: #d9ecfe;
  border-radius: 5px;
  border: 1px solid #d9ecfe;
}

.setting-item:hover {
  background-color: #ecf5fe;
  border-radius: 5px;
  border: 1px solid #ecf5fe;
}
</style>
