<template>
  <div class="header">
    <div class="nav-bar-wrapper">
      <div
        class="nav-bar-global nav-bar-content"
        :class="{ global_hide: switching }"
      >
        <slot name="major"><div class="major">此处插入主要内容</div></slot>
      </div>
      <div
        v-if="switch_able"
        class="nav-bar-local nav-bar-content"
        :class="{ local_show: switching }"
      >
        <slot name="minor"><div class="minor">此处插入次要内容</div></slot>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "SimpleHeaderBarPro",
  props: {
    switch_able: Boolean,
    switching: Boolean
  },
  data() {
    return {};
  },
  watch: {},
  mounted() {},
  methods: {}
};
</script>

<style scoped lang="scss">
.header {
  width: 100%;
  height: 56px;
}

/*修饰器*/
.nav-bar-wrapper {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 56px;
  background-color: #fff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  box-sizing: border-box;
}

/*内容*/
.nav-bar-content {
  height: 100%;
  width: 100%;
  margin: 0 auto;
  position: absolute;
  box-sizing: border-box;
  transition: 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
}

.nav-bar-global {
  height: 100%;
  transform: rotate3d(1, 0, 0, 0deg);
}

.global_hide {
  transform: rotate3d(1, 0, 0, 90deg) translateY(-30%);
  transform-origin: top;
}

.nav-bar-local {
  top: 0;
  left: 0;
  transform: rotate3d(1, 0, 0, -90deg);
  transform-origin: bottom;
}

.local_show {
  transform: rotate3d(1, 0, 0, 0deg);
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
