<template>
  <div class="lowin lowin-blue" id="login">
    <div class="lowin-brand">
      <img src="../../static/images/kodinger.jpg" alt="logo" />
    </div>
    <div class="lowin-wrapper">
      <div class="lowin-box lowin-login">
        <div class="lowin-box-inner">
          <form @click.prevent>
            <p>请先登录</p>
            <div class="lowin-group">
              <label for="account">
                账号 <a href="#" class="login-back-link">登录?</a>
              </label>
              <input
                id="account"
                v-model="account"
                class="lowin-input"
                placeholder="账号/邮箱"
              />
            </div>
            <div class="lowin-group password-group">
              <label for="login-password">
                密码 <a href="#" class="forgot-link">忘记密码?</a>
              </label>
              <input
                id="login-password"
                v-model="password"
                type="password"
                name="password"
                class="lowin-input"
              />
            </div>
            <button
              class="lowin-btn login-btn"
              @click="doLogin()"
              :disabled="login_disabled"
            >
              登 录
            </button>

            <div class="text-foot">
              没有账号？ <a href="#" class="register-link">注册</a>
            </div>
          </form>
        </div>
      </div>

      <div class="lowin-box lowin-register">
        <div class="lowin-box-inner">
          <form @click.prevent>
            <p>创建你的账号吧</p>
            <div class="lowin-group">
              <label for="register-username">用户名</label>
              <input
                id="register-username"
                @blur="validateUsername(username)"
                v-bind:class="{ 'valid-failed': username_valid_failed }"
                v-model="username"
                type="text"
                placeholder="6-20位"
                name="name"
                autocomplete="name"
                class="lowin-input"
              />
            </div>
            <div class="lowin-group">
              <label for="register-mail">邮箱</label>
              <input
                id="register-mail"
                @blur="validateMail(email)"
                :class="{ 'valid-failed': mail_valid_failed }"
                v-model="email"
                placeholder="输入有效邮箱用于登录以获取验证码"
                type="email"
                autocomplete="email"
                name="email"
                class="lowin-input"
              />
            </div>
            <div class="lowin-group">
              <label for="verifycode">验证码</label>
              <div>
                <input
                  v-model="verificationCode"
                  id="verifycode"
                  type="text"
                  placeholder="6位数字"
                  name="verifycode"
                  class="verify-input"
                  @blur="validateVerificationCode(verificationCode)"
                />
                <button
                  class="verify-btn"
                  @click="getVerifyCode"
                  :disabled="disabled_flag"
                >
                  {{ btn_text }}
                </button>
              </div>
            </div>
            <div class="lowin-group">
              <label for="register-password">密码</label>
              <input
                v-model="password"
                id="register-password"
                type="password"
                placeholder="6-20位登录密码"
                name="password"
                autocomplete="current-password"
                class="lowin-input"
                @blur="validatePassword(password)"
              />
            </div>
            <button
              class="lowin-btn register-btn"
              @click="doRegister()"
              :disabled="register_disabled"
            >
              注 册
            </button>

            <div class="text-foot">
              已有账号? <a style="cursor: pointer" class="login-link">登录</a>
            </div>
          </form>
        </div>
      </div>
    </div>

    <footer class="lowin-footer">
      Design By <a href="http://fb.me/itskodinger">@itskodinger</a>
    </footer>
  </div>
</template>

<script>
import { Auth } from "../../static/javascript/auth";
import { getVerificationCode, register } from "../api/user";
import store from "../store/index";

const TIME_COUNT = 60;
export default {
  name: "Login",
  data() {
    return {
      username: "",
      email: "",
      password: "",
      account: "",
      verificationCode: "",
      countdown_flag: 60,
      btn_text: "获取验证码",
      disabled_flag: false,
      register_disabled: false,
      login_disabled: false,
      mail_changed: false,
      username_changed: false,
      verificationCode_changed: false,
      password_changed: false,
      mail_valid_failed: false,
      username_valid_failed: false,
      verificationCode_valid_failed: false,
      password_valid_failed: false,
      lastPath: "1"
    };
  },
  watch: {
    email: function() {
      this.email = this.email.replace(/[, \s]/g, "").substring(0, 100);
      this.mail_valid_failed = false;
      this.mail_changed = true;
    },
    username: function() {
      this.username = this.username.replace(/[, \s]/g, "").substring(0, 20);
      this.username_valid_failed = false;
      this.username_changed = true;
    },
    verificationCode: function() {
      this.verificationCode = this.verificationCode
        .replace(/[, \s]/g, "")
        .substring(0, 6);
      this.verificationCode_valid_failed = false;
      this.verificationCode_changed = true;
    },
    password: function() {
      this.password = this.password.replace(/[, \s]/g, "").substring(0, 20);
      this.password_valid_failed = false;
      this.password_changed = true;
    }
  },
  mounted() {
    Auth.init({
      login_url: "#login",
      forgot_url: "#forgot"
    });
    this.ready();
    console.info("last:" + this.lastPath + ";");
  },
  methods: {
    //获取验证码
    getVerifyCode() {
      if (!this.validateMail(this.email)) {
        this.mail_valid_failed = true;
        return;
      }
      getVerificationCode(this.email)
        .then(response => {
          // 把'剩余倒计时时间'保存到cookie中,有效时间60s
          this.addCookie("countdownStart", new Date().getTime(), 60);
          this.btn_text = "(" + this.countdown_flag + "s)后重新发送";
          this.disabled_flag = true;
          this.countdown(TIME_COUNT);
          this.$elementMessage(response.message, "success", 3 * 1000);
        })
        .catch(error => {
          this.$elementMessage(error.message, "error", 3 * 1000);
          console.log("err:" + error);
        });
    },

    //注册
    doRegister() {
      let _this = this;
      this.register_disabled = true;
      this.$elementMessage("正准备跳转登录...", "success", 3 * 1000);
      //判空
      if (
        !this.username ||
        !this.email ||
        !this.password ||
        !this.verificationCode
      ) {
        this.$elementMessage("请完善信息", "warning", 3 * 1000);
        return;
      }
      //验证邮箱和用户名
      if (
        !this.validateMail(this.email) ||
        !this.validateUsername(this.username) ||
        !this.validateVerificationCode(this.verificationCode) ||
        !this.validatePassword(this.password)
      ) {
        return;
      }
      let registerInfo = {
        email: this.email,
        username: this.username,
        verificationCode: this.verificationCode,
        password: this.password
      };
      //注册逻辑
      register(registerInfo)
        .then(response => {
          this.register_disabled = true;
          this.$elementMessage(
            response.message + "正准备跳转登录...",
            "success",
            3 * 1000
          );
          setTimeout(function() {
            _this.account = _this.username;
            _this.doLogin();
          }, 1500);
        })
        .catch(error => {
          this.register_disabled = false;
          this.$elementMessage(error.message, "error", 3 * 1000);
        });
    },

    //登录
    doLogin() {
      let _this = this;
      let loginParam = {
        account: this.account,
        password: this.password
      };
      store
        .dispatch("Login", loginParam)
        .then(response => {
          this.login_disabled = true;
          store.commit("SET_ACCOUNT", loginParam.account);
          this.$elementMessage(
            response.message + "，正在跳转...",
            "success",
            1500
          );
          setTimeout(function() {
            _this.login_disabled = false;
            _this.$router.go(-1); //返回上一层
            // _this.$router.push({ path: "/" });
          }, 1500);
          return Promise.resolve(response);
        })
        .catch(error => {
          this.$elementMessage(error.message, "error", 3000);
        });
    },

    //倒计时
    countdown(count) {
      //页面显示的剩余秒数
      this.countdown_flag = count;
      //设置为1可以不显示0s，体验更好
      if (count > 0) {
        //设置提示的文字
        this.btn_text = "(" + this.countdown_flag + "s)后重新发送";
        //使能按钮
        this.disabled_flag = true;
        count--;
        let _this = this;
        //设置定时器
        setTimeout(function() {
          _this.countdown(count);
        }, 1000);
      } else {
        this.btn_text = "获取验证码";
        this.disabled_flag = false;
        //清除定时器
        clearTimeout();
      }
    },

    //发送验证码时添加cookie
    addCookie(name, value, expiresSecs) {
      let cookieString = name + "=" + escape(value);
      //判断是否设置过期时间,0代表关闭浏览器时失效
      if (expiresSecs > 0) {
        let date = new Date();
        date.setTime(date.getTime() + expiresSecs * 1000);
        cookieString = cookieString + ";expires=" + date.toUTCString();
      }
      document.cookie = cookieString;
    },

    //根据名字获取cookie的值
    getCookieValue(name) {
      let strCookie = document.cookie;
      let arrCookie = strCookie.split("; ");
      for (let i = 0; i < arrCookie.length; i++) {
        let arr = arrCookie[i].split("=");
        if (arr[0] === name) {
          return unescape(arr[1]);
        }
      }
    },

    //验证邮箱
    validateMail(mail) {
      //邮箱为空不校验返回true，获取验证码时再判空
      if (!mail) {
        this.mail_valid_failed = false;
        return false;
      }
      let mail_re = new RegExp(
        "^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$"
      );
      //邮箱变动才验证
      if (this.mail_changed && !mail_re.test(mail)) {
        this.mail_valid_failed = true;
        this.mail_changed = false;
        this.$elementMessage("邮箱格式不正确", "warning", 3 * 1000);
        return false;
      }
      return true;
    },

    //验证用户名
    validateUsername(username) {
      //邮箱为空不校验返回true，获取验证码时再判空
      if (!username) {
        this.mail_valid_failed = false;
        return false;
      }
      let username_re = new RegExp("^[A-Za-z0-9_-]{6,20}$");
      //邮箱变动才验证
      if (this.username_changed && !username_re.test(username)) {
        this.username_valid_failed = true;
        this.username_changed = false;
        this.$elementMessage("用户名格式不正确", "warning", 3 * 1000);
        return false;
      }
      return true;
    },

    //验证验证码
    validateVerificationCode(code) {
      //验证码为空不校验返回true，获取验证码时再判空
      if (!code) {
        this.verificationCode_valid_failed = false;
        return false;
      }
      let code_re = new RegExp("^[0-9]{6}$");
      //验证码变动才验证
      if (this.verificationCode_changed && !code_re.test(code)) {
        this.verificationCode_valid_failed = true;
        this.verificationCode_changed = false;
        this.$elementMessage("验证码格式不正确", "warning", 3 * 1000);
        return false;
      }
      return true;
    },

    //验证密码
    validatePassword(password) {
      //用户名为空不校验返回true，注册时再判空
      if (!password) {
        this.password_valid_failed = false;
        return false;
      }
      let username_re = new RegExp("^[a-zA-Z0-9#?!@$%^&*-]{6,20}$");
      //用户名变动才验证
      if (this.password_changed && !username_re.test(password)) {
        this.password_changed = false;
        this.password_valid_failed = true;
        this.$elementMessage("密码格式不正确", "warning", 3 * 1000);
        return false;
      }
      return true;
    },

    //页面加载时初始化倒计时
    ready() {
      // 页面加载完成后，从cookie中获取剩余倒计时秒数。默认值为0
      let v = this.getCookieValue("countdownStart")
        ? this.getCookieValue("countdownStart")
        : 0;
      if (v === 0) {
        return;
      }
      let current = new Date().getTime();
      let t = Math.round((current - v) / 1000);
      // 如果倒计时还未过，则设置倒计时间
      if (t > 0) {
        //开始倒计时
        this.countdown(TIME_COUNT - t);
      }
    }
  }
};
</script>

<style lang="scss">
@import "../../static/style/auth.css";
</style>
