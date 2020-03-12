<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <div class="workbench-menu">
      <div class="editor-switcher">
        <div class="switcher-label-wrapper">
          <span class="switcher-label">QL</span>
          <span class="switcher-label">MD</span>
        </div>
        <el-switch
          class="switcher-btn"
          v-model="style"
          active-color="#13ce66"
          inactive-color="#ff4949"
        >
        </el-switch>
      </div>
    </div>

    <div style="width: 100%; text-align: left">标题</div>
    <el-input></el-input>
    <div style="width: 100%; text-align: left">内容</div>
    <div style="height: 25px; width: 100%"></div>

    <div class="sidebar">
      <TimeLine
        :call-back="timeLineCallback"
        :items="items"
        :end-item="endItem"
      ></TimeLine>
    </div>

    <div class="content">
      <Editor
        v-if="style"
        v-model="content"
        ref="myQuillEditor"
        :options="editorOption"
        @blur="onEditorBlur($event)"
        @focus="onEditorFocus($event)"
        @change="onEditorChange($event)"
      ></Editor>

      <MarkdownEditor v-if="!style"></MarkdownEditor>
    </div>
  </div>
</template>

<script>
import MarkdownEditor from "../components/MarkdownEditor";
import TimeLine from "../components/TimeLine";
export default {
  name: "WorkBench",
  components: { TimeLine, MarkdownEditor },
  mounted() {
    this.timeLineCallback = function(index, content) {
      console.info("index:" + index + "\n" + "content:" + content);
    };
  },
  data() {
    return {
      content: null,
      editorOption: {},
      style: true,
      timeLineCallback: null,
      items: [
        {
          index: "2020-8-14 13:20:30",
          content: "开始毕设。。"
        },
        { index: "2020-8-15 13:20:30", content: "写前端。。" },
        {
          index: "2020-8-16 13:20:30",
          content: "还在写前端。。"
        },
        {
          index: "2020-8-17 13:20:30",
          content: "仍在写前端。。"
        },
        { index: "2020-8-18 13:20:30", content: "不想写前端。。" },
        { index: "2020-8-19 13:20:30", content: "还得写前端。。。。。" }
      ],
      endItem: {
        index: "2020-8-20 13:20:30",
        content: "正在写前端。。。。"
      }
    };
  },
  methods: {
    onEditorBlur() {
      //失去焦点事件
    },
    onEditorFocus() {
      //获得焦点事件
    },
    onEditorChange() {
      //内容改变事件
    }
  }
};
</script>

<style>
.workbench-menu {
  width: 100%;
  height: 58px;
  border: 0 solid #f0f0f0;
  border-bottom-width: 1px;
}

.sidebar {
  width: 180px;
  height: 100%;
  position: relative;
  float: left;
}

.content {
  width: calc(100% - 180px);
  height: 100%;
  position: relative;
  float: left;
}

.editor-switcher {
  width: 75px;
  height: 100%;
  float: right;
}

.switcher-label-wrapper {
  position: relative;
  float: left;
  width: 35px;
  height: 56px;
  text-align: right;
  top: 50%;
  transform: translate3d(0, -50%, 0);
  padding: 5px 0;
  box-sizing: border-box;
}

.switcher-label {
  width: 100%;
  height: 20px;
  line-height: 28px;
  float: right;
  font-size: 18px;
  font-family: "monospace";
}

.switcher-btn {
  transform: translate3d(0, -50%, 0) rotate3d(0, 0, 1, 90deg);
  float: right;
  top: 50%;
}

body {
  margin: 0 !important;
}
</style>
