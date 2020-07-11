<template>
  <div class="header-menu">
    <div class="nav">
      <a @click="selectTab('group')">小组</a>
      <a @click="selectTab('follow')" style="display: none">关注</a>
      <a @click="selectTab('notice')" style="display: none">消息</a>
    </div>
<!--    <div class="search">-->
<!--      <input class="search-input" placeholder="搜索" v-model="searchContent" />-->
<!--      <div class="search-btn" @click="search()">-->
<!--        <i class="el-icon-search"></i>-->
<!--      </div>-->
<!--    </div>-->
    <div class="login-register" v-if="!checkLogedIn()">
      <div class="login-btn" @click="toLogin()">
        登录/注册
      </div>
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
          class="create-btn"
          type="primary"
          icon="el-icon-edit"
          circle
          @click="toWorkBench()"
        ></el-button>
      </div>
      <div class="btn-wrapper">
        <el-button
          class="home-btn"
          type="primary"
          icon="el-icon-collection"
          circle
          @click="toResource()"
        >
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import "element-ui/lib/theme-chalk/index.css";
import { mapState } from "vuex";
import store from "../store";
export default {
  name: "HeaderMenu",
  watch: {
    info: function(info) {
      let icon = info.icon;
      if (!icon) {
        icon = "http://localhost/static/icon/DEFAULT.png";
      } else {
        icon = "http://localhost" + icon;
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
        let icon = state.user.info.icon;
        if (!icon) {
          icon = "http://localhost/static/icon/DEFAULT.png";
        } else {
          icon = "http://localhost" + icon;
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
      size: 40,
      searchContent: ""
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
    toLogin: function() {
      this.$router.push("/loginPage");
    },
    toWorkBench() {
      let userId = this.$store.state.user.info.id;
      this.$router.push("/workBench/" + userId);
    },
    toResource() {
      this.$router.push("/resource/note");
    },
    selectTab(option) {
      this.$store.dispatch("getAllGroups", null);
      let tab = ["group", "follow", "notice"];
      let els = document.getElementsByClassName("nav").item(0).children;
      tab.forEach((val, index) => {
        els[index].classList.remove("selected");
        if (val === option) {
          els[index].classList.add("selected");
        }
      });
      if (tab.indexOf(option) < 0) return;
      if (this.$route.path === "/" + option) return;
      this.$router.push("/" + option);
    },
    initialTab() {
      let option = this.$route.path.split("/");
      let tab = ["group", "follow", "notice"];
      option = option[option.length - 1];
      if (tab.indexOf(option) < 0) return;
      this.selectTab(option);
    },
    search() {
      this.selectTab("");
      this.$store.commit("SET_SEARCHWD", this.searchContent);
      if (this.$route.path === "/search") {
        return;
      }
      this.$router.push("/search");
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
  box-sizing: border-box;
  position: relative;
  min-width: 780px;
}

.nav {
  position: relative;
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

.search {
  display: inline-block;
  box-sizing: border-box;
  position: relative;
  float: left;
  width: calc(100% - 600px);
  min-width: 200px;
  max-width: 350px;
  height: 38px;
  margin-top: 10px;
  background-color: #eaeaea;
  border-radius: 40px;
  box-sizing: border-box;
}

.search-input {
  float: left;
  height: 38px;
  width: calc(100% - 45px);
  cursor: text;
  padding-left: 15px;
  box-sizing: border-box;
  background: none;
  outline: none;
  border: none;
  color: #929292;
  font-size: 15px;
}

.search-btn {
  float: right;
  width: 38px;
  height: 38px;
  border-radius: 100%;
  font-size: 20px;
  font-weight: bold;
  line-height: 38px;
  color: #919191;
  cursor: pointer;
}

.func-btn {
  position: relative;
  width: 120px;
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
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
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
