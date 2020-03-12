<template>
  <div class="header">
    <div class="nav-bar-wrapper" @click="s()">
      <div
        class="nav-bar-global nav-bar-content"
        :class="{ global_hide: !isUp }"
      >
        <slot name="major"><div class="major">此处插入主要内容</div></slot>
      </div>
      <div
        v-if="switch_able"
        class="nav-bar-local nav-bar-content"
        :class="{ local_show: !isUp }"
      >
        <slot name="minor"><div class="minor">此处插入次要内容</div></slot>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  name: "HeaderBar",
  data() {
    return {
      g: true,
      scrollTop: 0, // 初始化滚动条为位置为0
      topValue: 0, // 标识位
      isUp: true, // 判断是否是向上滚动
      changeUpPos: 0, // 设置从向下滚动到向上滚动时的位置（距离滚动条顶部）
      changeDownPos: 0 // 设置从向上滚动到向下滚动时的位置（距离滚动条顶部）
    };
  },
  computed: {
    ...mapState({
      switch_able: state => state.headerbar.switch_able
    })
  },
  watch: {
    switch_able: function(bool) {
      if (bool) {
        this.scrollTop = 0; // 初始化滚动条为位置为0
        this.topValue = this.getScrollTop();
        document.documentElement.scrollTo(0, 0);
        document.addEventListener("scroll", this.scrollListener);
      } else {
        this.isUp = true;
        document.removeEventListener("scroll", this.scrollListener);
      }
    }
  },
  mounted() {
    if (this.switch_able) {
      document.onscroll = this.scrollListener();
    }
  },
  methods: {
    s() {
      this.$store.commit("SET_SWITCH_ABLE", this.g);
      this.g = !this.g;
    },
    scrollListener() {
      this.scrollTop = this.getScrollTop(); // 滚动开始一刻，即滚动条位置发生变化的一刻，滚动条的位置，比较变化前后的值判断方向
      if (this.scrollTop <= this.topValue) {
        // 当后者滚动条大于前者滚动条时，即认为滚动条向上运动，但是我们设置一个临界值，当大于这个临界值时，即认为是用户有意向上滚动
        this.changeUpPos = this.scrollTop; // 刚好从向下滚动到向上滚动改变方向时的位置
        if (this.changeDownPos - this.scrollTop > 120) {
          // 这个是else 里面记录的值减滚动条位置 大于120 即认为是向上滚动
          this.isUp = true;
        }
      } else {
        this.changeDownPos = this.scrollTop;
        if (this.scrollTop - this.changeUpPos > 120) {
          this.isUp = false;
        }
      }
      let _this = this;
      setTimeout(function() {
        _this.topValue = _this.scrollTop; //异步标记当前位置，用下次位置与这次的比较，判断滚动条在这期间的滚动状态
      }, 0);
    },
    getScrollTop() {
      // 获取滚动条位置
      let scrollTop = 0;
      if (document.documentElement && document.documentElement.scrollTop) {
        scrollTop = document.documentElement.scrollTop;
      } else if (document.body) {
        scrollTop = document.body.scrollTop;
      }
      return scrollTop;
    }
  }
};
</script>

<style scoped lang="scss">
.header {
  width: 100%;
}

/*修饰器*/
.nav-bar-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 56px;
  overflow: hidden;
  background-color: #fff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  z-index: 100;
}

/*内容*/
.nav-bar-content {
  height: 100%;
  width: 100%;
  margin: 0 auto;
  position: absolute;
  box-sizing: border-box;
  transition: transform 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
}

.nav-bar-global {
  height: 100%;
  background-color: #ffffff;
  transition: 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
}

.nav-bar-local {
  top: 0;
  left: 0;
  transform: translateY(100%);
  background-color: #ffffff;
}

.local_show {
  transform: translateY(0);
}

.global_hide {
  transform: translateY(-100%);
}

.major {
  height: 100%;
  background-color: aqua;
  line-height: 56px;
}

.minor {
  height: 100%;
  line-height: 56px;
  background-color: orange;
}
</style>
