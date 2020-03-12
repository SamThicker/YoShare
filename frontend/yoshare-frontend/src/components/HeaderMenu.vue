<template>
  <div class="header-menu">
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
      <a class="avatar">
        <el-avatar class="user-icon" :size="size" :src="icon"></el-avatar>
      </a>
      <transition name="el-fade-in">
        <ul v-show="show" class="dropdown-menu">
          <li class="menu-item">
            <a href="#/userHome">
              <i class="el-icon-s-home menu-icon"></i>
              <span>我的主页</span>
            </a>
          </li>
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
    <div class="create-btn-wrapper">
      <el-button
        class="create-btn"
        type="primary"
        icon="el-icon-edit"
        circle
        @click="toWorkBench()"
      ></el-button>
    </div>
    <div class="resource-btn-wrapper">
      <el-button
        class="resource-btn"
        type="primary"
        icon="el-icon-upload"
        circle
        @click="toResource()"
      >
      </el-button>
    </div>
    <div class="home-btn-wrapper">
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
    }
  },
  computed: {
    ...mapState({
      icon: function(state) {
        let icon = state.user.icon;
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
    })
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
    toLogin: function() {
      this.$router.push("/loginPage");
    },
    toWorkBench() {
      this.$router.push("/workBench");
    },
    toResource() {
      this.$router.push("/resource");
    },
    toHome() {
      this.$router.push("/");
    }
  },
  mounted() {
    document.addEventListener("scroll", this.scrollListener);
  }
};
</script>

<style scoped>
.header-menu {
  width: 100%;
  height: 100%;
}

.create-btn-wrapper,
.resource-btn-wrapper,
.home-btn-wrapper {
  position: relative;
  width: 60px;
  height: 100%;
  float: right;
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
  border: 1px solid #ddd;
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
  width: 95px;
  padding-right: 30px;
  position: relative;
  float: right;
  cursor: pointer;
}

.user:hover {
  background-color: #f5f5f5;
}

.avatar {
  position: relative;
  width: 40px;
  height: 40px;
  margin: 8px 24px 8px 16px;
}
.avatar:before {
  content: "";
  position: absolute;
  top: 25px;
  left: 18px;
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
  border-top: 6px solid #999;
  display: block;
}

.user-icon {
  height: 40px;
  width: 40px;
  box-sizing: border-box;
  border-radius: 50%;
  border: 1px solid #ddd;
  vertical-align: center;
  position: relative;
  float: right;
  top: 50%;
  transform: translate3d(0, -50%, 0);
}

.dropdown-menu {
  height: auto;
  padding: 5px;
  border-radius: 5px;
  position: absolute;
  box-sizing: border-box;
  z-index: 1000;
  top: 56px;
  right: 10px;
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
