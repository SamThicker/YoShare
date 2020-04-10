<template>
  <div class="introduction introduction-panel">
    <div
      class="introduction introduction-wrapper"
      v-bind:class="{ active: introduction_active }"
    >
      <div class="introduction introduction-rope"></div>
      <div class="introduction introduction-bar">
        <slot name="title">
          <h1>在这里插入标题</h1>
        </slot>
        <slot name="content">
          <p>在这里插入内容</p>
        </slot>
      </div>
    </div>
    <div
      class="introduction introduction-trigger"
      v-bind:class="{ active: introduction_active }"
    >
      <div class="introduction trigger-rope"></div>
      <div
        class="introduction trigger-point"
        @click="changeActiveStatus()"
        v-bind:class="{ active: introduction_active }"
      >
        <div class="arrow arrow-down"></div>
        <div class="arrow arrow-down"></div>
        <div class="arrow arrow-down"></div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "IntroductionPanel",
  data() {
    return {
      introduction_active: false
    };
  },
  methods: {
    changeActiveStatus() {
      this.introduction_active = !this.introduction_active;
    }
  }
};
</script>

<style scoped>
/*全局*/
.introduction {
  z-index: 100;
  -webkit-transition: 0.3s cubic-bezier(0.37, 1.44, 0.57, 0.77);
  transition: 0.3s cubic-bezier(0.37, 1.44, 0.57, 0.77);
}

/*左边部分*/
.introduction.introduction-wrapper {
  width: 100%;
  /*相对浏览器定位*/
  position: fixed;
  top: -900px;
  text-align: center;
}

/*左边部分激活后的位置*/
.introduction.introduction-wrapper.active {
  /*-5是为了遮盖绳子上方*/
  top: -5px;
}

/*左边牵引绳索，实际上是div左右两边*/
.introduction.introduction-rope {
  width: 200px;
  height: 100px;
  border-left: 5px rgba(0, 129, 198, 0.8) solid;
  border-right: 5px rgba(0, 129, 198, 0.8) solid;
  box-shadow: 0 0 5px 0 rgba(13, 148, 225, 0.8),
    0 0 5px 0 rgba(13, 148, 225, 0.8) inset;
  -webkit-box-shadow: 0 0 5px 0 rgba(13, 148, 225, 0.8),
    0 0 5px 0 rgba(13, 148, 225, 0.8) inset;
  position: relative;
  margin: 0 auto;
}

/*左边显示面板*/
.introduction.introduction-bar {
  width: 45%;
  min-width: 350px;
  max-width: 500px;
  height: 30%;
  margin: 0 auto;
  text-align: left;
  color: rgba(13, 148, 225, 0.9);
  padding: 40px 60px;
  top: -5px;
  position: relative;
  border-radius: 5px;
  background-color: rgba(254, 253, 255, 0.9);
  box-shadow: 0 7px 25px rgba(0, 0, 0, 0.18);
  -webkit-box-shadow: 0 7px 25px rgba(0, 0, 0, 0.18);
  overflow: auto;
  box-sizing: border-box;
}

/*开关*/
.introduction.introduction-trigger {
  top: 0;
  left: 80%;
  /*相对浏览器*/
  position: fixed;
  text-align: center;
}

/*开关激活后高度*/
.introduction.introduction-trigger.active {
  top: -150px;
}

/*开关绳索*/
.introduction.trigger-rope {
  width: 5px;
  height: 300px;
  margin: 0 auto;
  background-color: rgba(0, 129, 198, 0.8);
  box-shadow: 0 0 5px 0 rgba(13, 148, 225, 0.8);
  -webkit-box-shadow: 0 0 5px 0 rgba(13, 148, 225, 0.8);
}

/*开关绳结*/
.introduction.trigger-point {
  margin: 0 auto;
  width: 25px;
  height: 60px;
  background-color: rgba(0, 129, 198, 0.9);
  box-shadow: 0 0 5px 0 rgba(13, 148, 225, 0.8);
  border-radius: 5px;
  text-align: center;
  cursor: pointer;
  padding-bottom: 8px;
}

/*开关绳结激活状态*/
.introduction.trigger-point.active {
  padding-top: 3px;
  padding-bottom: 3px;
}

/*开关绳结激活后箭头旋转*/
.introduction.trigger-point.active > .arrow-down {
  -webkit-transform: rotate3d(0, 0, 1, 225deg);
  transform: rotate3d(0, 0, 1, 225deg);
}

/*绳结下方提示*/
.introduction.trigger-point:not(.active)::before {
  content: "点击查看帮助";
  color: rgba(13, 148, 225, 0.8);
  width: 100px;
  position: absolute;
  left: 50%;
  top: 100%;
  -webkit-transform: translate3d(-50%, 0, 0);
  transform: translate3d(-50%, 0, 0);
}

/*div两邻边形成箭头*/
.arrow {
  border: solid #ffffff;
  border-width: 0 3px 3px 0;
  display: inline-block;
  padding: 5px;
  margin: 0 auto;
}

/*旋转div形成向下的箭头*/
.arrow-down {
  transform: rotate3d(0, 0, 1, 45deg);
  -webkit-transform: rotate3d(0, 0, 1, 45deg);
}
</style>
