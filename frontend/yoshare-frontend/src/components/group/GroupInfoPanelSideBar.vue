<template>
  <ul>
    <li
      class="setting-item"
      v-for="(setting, index) in settings"
      v-bind:key="setting.name"
      :class="active === index ? 'active' : ''"
      @click="activeEl(index)"
    >
      <router-link :to="setting.link" class="link">
        {{ setting.name }}
      </router-link>
    </li>
  </ul>
</template>

<script>
export default {
  name: "GroupInfoPanelSideBar",
  data() {
    return {
      settings: [
        {
          name: "基本信息",
          icon: "",
          link: "",
          label: "simple"
        }
      ],
      active: -1,
      link: []
    };
  },
  watch: {
    groupId: function() {
      this.setLink();
      this.setActiveEl();
    },
    path: function() {
      this.setActiveEl();
      this.setLink();
    }
  },
  computed: {
    groupId: function() {
      return this.$route.params.groupId;
    },
    path: function() {
      return this.$route.path;
    }
  },
  mounted() {
    this.setActiveEl();
    this.setLink();
  },
  methods: {
    activeEl(index) {
      this.active = index;
    },
    setLink() {
      this.settings.forEach(setting => {
        setting.link = "/group/" + this.groupId + "/setting/" + setting.label;
      });
    },
    setActiveEl() {
      let pathParts = this.$route.path.split("/");
      let option = pathParts[pathParts.length - 1];
      let _this = this;
      this.settings.forEach(function(setting, index) {
        if (setting.label === option) {
          _this.activeEl(index);
        }
      });
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

li {
  position: relative;
  width: 100%;
  height: 100%;
}

.link {
  position: relative;
  user-select: none;
  display: block;
  line-height: 50px;
  box-sizing: border-box;
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
}

.active {
  background-color: #d9ecfe;
  border-radius: 5px;
  border: 1px solid #d9ecfe;
}

.setting-item:hover:not(.active) {
  background-color: #ecf5fe;
  border-radius: 5px;
  border: 1px solid #ecf5fe;
}
</style>
