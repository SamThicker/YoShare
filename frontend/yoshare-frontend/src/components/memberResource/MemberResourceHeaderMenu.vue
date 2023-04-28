<template>
  <div class="header-menu">
    <div class="nav">
      <a @click="selectTab('note')">笔记</a>
      <a @click="selectTab('file')">文件</a>
      <a @click="selectTab('favorite')">收藏</a>
    </div>
    <div
      class="user"
      @mouseenter="show = true"
      @mouseleave="show = false"
      v-if="checkLogedIn()"
    >
      <!--用户头像-->
      <a class="avatar">
        <img :src="icon" />
      </a>
      <transition name="el-fade-in">
        <ul v-show="show" class="dropdown-menu">
          <li class="menu-item">
            <a href="#/userInfo/simpleInfo">
              <i class="el-icon-user-solid menu-icon"></i>
              <span>个人信息</span>
            </a>
          </li>
          <li class="menu-item">
            <a @click="logOut">
              <i class="el-icon-close menu-icon"></i>
              <span>退出</span>
            </a>
          </li>
        </ul>
      </transition>
    </div>
    <div class="func-btn">
      <div class="btn-wrapper">
        <el-button
          class="home-btn"
          type="primary"
          icon="el-icon-s-home"
          circle
          @click="toHome()"
        >
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import "element-ui/lib/theme-chalk/index.css";
import { mapState } from "vuex";
import store from "@/store";
export default {
  name: "MemberResourceHeaderMenu",
  watch: {
    info: function(info) {
      let baseUrl = window.location.host;
      let icon = info.icon;
      if (!icon) {
        icon = "http://" + baseUrl + "/static/icon/DEFAULT.png";
      } else {
        icon = "http://" + baseUrl + icon;
      }
      return icon;
    },
    path: function(val) {
      if (val === "/") {
        this.$router.push("/group");
      }
      this.initialTab();
    }
  },
  computed: {
    ...mapState({
      icon: function(state) {
        let baseUrl = window.location.host;
        let icon = state.user.info.icon;
        if (!icon) {
          icon = "http://" + baseUrl + "/static/icon/DEFAULT.png";
        } else {
          icon = "http://baseUrl" + icon;
        }
        return icon;
      },
      account: state => state.user.account,
      token: state => state.user.token,
      info: state => state.user.info
    }),
    path: function() {
      return this.$route.path;
    }
  },
  data() {
    return {
      show: false,
      size: 40
    };
  },
  methods: {
    scrollListener() {
      this.show = false;
    },
    logOut: function() {
      store.dispatch("LogOut");
      if (
        this.$router.currentRoute.matched.some(
          record => record.meta.requireAuth
        )
      ) {
        this.$router.push("/");
      }
    },
    checkLogedIn: function() {
      return !!store.getters.account;
    },
    toHome() {
      this.$router.push("/");
    },
    selectTab(option) {
      let tab = ["note", "file", "favorite"];
      let els = document.getElementsByClassName("nav").item(0).children;
      tab.forEach((val, index) => {
        els[index].classList.remove("selected");
        if (val === option) {
          els[index].classList.add("selected");
        }
      });
      if (this.$route.path === "/resource/" + option) return;
      this.$router.push("/resource/" + option);
    },
    initialTab() {
      let option = this.$route.path.split("/");
      let tab = ["note", "file", "favorite"];
      option = option[option.length - 1];
      if (tab.indexOf(option) < 0) return;
      this.selectTab(option);
    }
  },
  mounted() {
    document.addEventListener("scroll", this.scrollListener);
    this.initialTab();
  }
};
</script>

<style scoped>
.header-menu {
  width: 100%;
  height: 100%;
}

.nav {
  width: auto;
  height: 100%;
  margin-left: 100px;
  line-height: 100%;
  display: flex;
  float: left;
}

.nav a {
  line-height: 100%;
  display: flex;
  align-items: center;
  padding: 0 20px;
  font-size: 17px;
  color: #333;
  cursor: pointer;
  user-select: none;
  font-weight: bold;
}

.nav a:hover {
  background-color: #f5f5f5;
  color: #7bb6fb;
}

.nav a.selected {
  color: #4594fa;
}

.func-btn {
  position: relative;
  width: 180px;
  height: 58px;
  float: right;
  top: 50%;
  transform: translate3d(0, -50%, 0);
}

.btn-wrapper {
  position: relative;
  width: 60px;
  height: 100%;
  float: right;
}

.btn-wrapper:hover {
  background-color: #f5f5f5;
}

.create-btn,
.resource-btn,
.home-btn {
  height: 40px;
  position: relative;
  top: 50%;
  transform: translate3d(0, -50%, 0);
}

.login-register {
  position: relative;
  height: 100%;
  float: right;
  width: 106px;
  margin-right: 20px;
}

.login-btn {
  position: relative;
  width: 100%;
  height: 40px;
  line-height: 40px;
  text-align: center;
  font-family: "Microsoft YaHei";
  font-size: 16px;
  color: #fff;
  border-radius: 30px;
  background-color: #569ffb;
  top: 50%;
  transform: translate3d(0, -50%, 0);
  cursor: pointer;
}

.user {
  display: inline-block;
  height: 100%;
  width: 75px;
  margin-right: 10px;
  position: relative;
  float: right;
}

.user:hover {
  background-color: #f5f5f5;
}

.avatar {
  position: relative;
  top: 50%;
  width: 40px;
  height: 40px;
  transform: translate3d(0, -50%, 0);
  display: block;
  cursor: pointer;
  left: 12px;
}

.avatar img {
  position: relative;
  object-fit: cover;
  width: 40px;
  height: 40px;
  border: 1px solid #ddd;
  border-radius: 50%;
}

.avatar:before {
  content: "";
  position: absolute;
  right: -15px;
  top: 50%;
  transform: translate3d(0, -50%, 0);
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
  border-top: 6px solid #999;
}

.dropdown-menu {
  height: auto;
  padding: 5px;
  border-radius: 5px;
  position: absolute;
  box-sizing: border-box;
  z-index: 1000;
  right: 0;
  top: 56px;
  width: 140px;
  background-color: #fff;
  margin-top: 0;
  border-color: transparent;
  box-shadow: 0 2px 8px 2px rgba(0, 0, 0, 0.1);
  filter: drop-shadow(0 2px 8px rgba(0, 0, 0, 0.1));
  -webkit-filter: drop-shadow(0 2px 8px rgba(0, 0, 0, 0.1));
}

.menu-item a {
  text-decoration: none;
  color: #333;
}

ul {
  list-style-type: none;
}

.menu-item {
  width: 100px;
  line-height: 20px;
  background-color: #fff;
  margin: 30px auto;
  cursor: pointer;
}

.menu-icon {
  margin-right: 5px;
  color: #569ffb;
  float: left;
}
</style>
