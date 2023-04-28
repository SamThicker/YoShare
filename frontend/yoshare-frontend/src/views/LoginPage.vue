<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <login></login>
    <introduction-panel>
      <template v-slot:title>
        <div>
          <h1 style="margin: 0 auto;">注册指引</h1>
        </div>
      </template>
      <template v-slot:content>
        <div v-for="(introduction, field) in introductions" v-bind:key="field">
          <p>{{ field }}</p>
          <p>{{ introduction }}</p>
          <br />
        </div>
      </template>
    </introduction-panel>
  </div>
</template>

<script>
import Login from "../components/Login";
import IntroductionPanel from "../components/IntroductionPanel";
export default {
  name: "LoginPage",
  beforeRouteEnter(to, from, next) {
    console.info("beforeRouteEnter第一个参数to", to);
    console.log("beforeRouteEnter第二个参数from", from);
    console.log("beforeRouteEnter第三个参数next", next);
    next(vm => {
      //因为当钩子执行前，组件实例还没被创建
      // vm 就是当前组件的实例相当于上面的 this，所以在 next 方法里你就可以把 vm 当 this 来用了。
      //当前组件的实例
      vm.lastPath = from;
    });
  },
  components: { IntroductionPanel, Login },
  data() {
    return {
      introductions: {
        "用户名：":
          '6-20位，可包含英文字母、阿拉伯数字和下划线("_")以及横线("-")，其中，英文字母大小写不敏感。',
        "邮箱：":
          "邮箱用于接收注册验证码和登录，后期通知也通过该邮箱下发，请输入有效的邮箱地址。",
        "验证码：":
          "6位数字。验证码用于校验输入的邮箱是否属于您本人，验证码有效期为5分钟。",
        "密码：":
          "6-20位字符。密码用于账号登录，请保管好您的密码，不要轻易透露给他人。密码不能包含空格以及，此外无特殊要求。"
      }
    };
  }
};
</script>

<style scoped></style>
