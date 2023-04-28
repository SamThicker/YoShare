<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div class="note-reading-wrap" v-loading="noteLoading">
    <div class="header-bar">
      <simple-header-bar-pro :switching="!isUp" :switch_able="true">
        <template v-slot:major>
          <div class="major-header">
            <div class="note-menu">
              <div class="font-size-trigger" @click.stop="fontSizeTrigger()">
                字号{{ fontSize }}
              </div>
              <div class="note-edit-trigger" @click.stop="noteEditTrigger()">
                编辑
              </div>
              <div class="font-size-slider" v-show="fontSizeSlider">
                <el-slider v-model="fontSize"></el-slider>
              </div>
            </div>
          </div>
        </template>
        <template v-slot:minor>
          <div class="minor-header">
            <div class="note-menu">
              <span class="note-title">{{ note ? note.title : "标题" }}</span>
              <div class="btn-to-top" @click="toTop()">
                <i class="el-icon-top"></i>
              </div>
            </div>
          </div>
        </template>
      </simple-header-bar-pro>
    </div>
    <div class="note-wrap" @scroll.stop="scrollListener">
      <markdown-it-vue
        v-show="currentContent ? currentContent.editor === 'MAVON' : false"
        :content="markdownContent"
        :options="options"
        class="note"
      />
      <div>
        <div
          v-html="
            currentContent && currentContent.editor === 'QUILL'
              ? currentContent.content
              : null
          "
          class="note"
        ></div>
      </div>
    </div>
  </div>
</template>

<script>
import SimpleHeaderBarPro from "@/components/group/GroupNoteSimpleHeaderBarPro";
import { getGroupNote } from "../../api/groupNote";
import { addReadNoteLog } from "../../api/log";

export default {
  name: "GroupNoteReading",
  components: { SimpleHeaderBarPro },
  props: {},
  mounted() {
    this.getNote();
  },
  data() {
    return {
      scrollTop: 0, // 初始化滚动条为位置为0
      topValue: 0, // 标识位
      isUp: true,
      changeUpPos: 0, // 设置从向下滚动到向上滚动时的位置（距离滚动条顶部）
      changeDownPos: 0, // 设置从向上滚动到向下滚动时的位置（距离滚动条顶部)
      fontSize: 10,
      fontSizeSlider: false,
      options: {
        markdownIt: {
          linkify: true
        },
        linkAttributes: {
          attrs: {
            target: "_blank",
            rel: "noopener"
          }
        }
      },
      markdownContent: "",
      currentContent: "",
      scrollAble: true,
      note: null,
      noteLoading: false
    };
  },
  computed: {
    noteId: function() {
      return this.$route.params.noteId;
    },
    groupId: function() {
      return this.$route.params.groupId;
    }
  },
  watch: {
    noteId: function() {
      if (!this.noteId) return;
      this.getNote();
    },
    scrollToTop: function(bool) {
      if (bool) this.toTop();
    },
    fontSize: function(size) {
      let el = document.querySelector(".note-wrap");
      el.removeEventListener("scroll", this.scrollListener);
      this.scrollAble = false;
      document.querySelector(".note").style.fontSize = size + "px";
      this.scrollAble = true;
      el.addEventListener("scroll", this.scrollListener);
    },
    note: function(val) {
      let contents = val.contents;
      this.currentContent = contents[contents.length - 1];
      this.markdownContent = this.currentContent.content;
    }
  },
  destroyed() {
    document.removeEventListener("click", this.fontSizeSliderListener);
  },
  methods: {
    getScrollTop() {
      let wrap = document.querySelector(".note-wrap");
      return wrap.scrollTop;
    },
    scrollListener() {
      this.fontSizeSlider = false;
      this.scrollTop = this.getScrollTop(); // 滚动开始一刻，即滚动条位置发生变化的一刻，滚动条的位置，比较变化前后的值判断方向
      if (this.scrollTop <= this.topValue) {
        // 当后者滚动条大于前者滚动条时，即认为滚动条向上运动，但是我们设置一个临界值，当大于这个临界值时，即认为是用户有意向上滚动
        this.changeUpPos = this.scrollTop; // 刚好从向下滚动到向上滚动改变方向时的位置
        if (this.changeDownPos - this.scrollTop > 60) {
          // 这个是else 里面记录的值减滚动条位置 大于120 即认为是向上滚动
          this.isUp = true;
        }
      } else {
        this.changeDownPos = this.scrollTop;
        if (this.scrollTop - this.changeUpPos > 60) {
          this.isUp = false;
        }
      }
      let _this = this;
      setTimeout(function() {
        _this.topValue = _this.scrollTop; //异步标记当前位置，用下次位置与这次的比较，判断滚动条在这期间的滚动状态
      }, 0);
    },
    fontSizeTrigger() {
      this.fontSizeSlider = !this.fontSizeSlider;
      if (this.fontSizeSlider) {
        document.addEventListener("click", this.fontSizeSliderListener);
      } else {
        document.removeEventListener("click", this.fontSizeSliderListener);
      }
    },
    fontSizeSliderListener(e) {
      let el = document.querySelector(".font-size-slider");
      if (!el.contains(e.target)) {
        this.fontSizeSlider = false;
        document.removeEventListener("click", this.fontSizeSliderListener);
      }
    },
    //回到顶部
    toTop() {
      if (!this.scrollAble) return;
      let _this = this;
      let wrap = document.querySelector(".note-wrap");
      let timer = setInterval(function() {
        let scrollTop = _this.getScrollTop();
        let speed = Math.floor(-scrollTop / 5);
        if (scrollTop === 0) {
          clearInterval(timer);
        }
        wrap.scrollTo(0, scrollTop + speed);
      }, 10);
    },
    noteEditTrigger() {
      let userId = this.$store.state.user.info.id;
      let noteId = this.note.id;
      let query = {};
      query.groupId = this.groupId;
      let path = "/workBench/" + userId + "/" + noteId;
      this.$router.push({ path: path, query: query });
    },
    getNote() {
      //清除定时器
      clearTimeout(this.timer);
      if (!this.noteId) return;
      this.noteLoading = true;
      let _this = this;
      getGroupNote(_this.groupId, _this.noteId)
        .then(function(res) {
          _this.note = res.data;
          _this.noteLoading = false;
          //设置定时器
          _this.timer = setTimeout(_this.addMemberReadRecord, 1000 * 30);
        })
        .catch(function(err) {
          console.info("err:" + err);
          _this.$elementMessage("获取笔记失败", "error", 1500);
          _this.noteLoading = false;
        });
    },
    //添加阅读记录
    addMemberReadRecord() {
      let userId = this.$store.state.user.info.id;
      let groupId = this.$route.params.groupId;
      if (!groupId) groupId = "";
      let noteType;
      let title = this.note.title;
      if (groupId) {
        noteType = "GROUP";
      } else {
        noteType = "MEMBER";
      }
      //发送请求
      addReadNoteLog(userId, this.noteId, noteType, groupId, title)
        .then()
        .catch();
    }
  }
};
</script>

<style scoped>
.note-reading-wrap {
  border: 1px solid #eee;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
}

.header-bar {
  position: relative;
  height: 56px;
  width: 100%;
  box-sizing: border-box;
  padding: 5px;
}

.note-menu {
  display: block;
  position: relative;
  height: 100%;
  padding: 0 10px;
}

.note-wrap {
  height: calc(100% - 56px);
  overflow-y: scroll;
  width: 100%;
}

.note {
  overflow: hidden;
  white-space: pre-wrap;
  word-wrap: break-word;
  text-align: left;
  padding: 20px;
  box-sizing: border-box;
}

.major-header,
.minor-header {
  padding: 7px;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
}

.minor-header {
}

.font-size-trigger {
  position: relative;
  background-color: #e8f2fe;
  border-radius: 5px;
  font-size: 10px;
  width: 55px;
  height: 30px;
  line-height: 30px;
  box-sizing: border-box;
  float: right;
  top: 50%;
  transform: translate3d(0, -50%, 0);
  user-select: none;
  cursor: pointer;
}

.note-edit-trigger {
  position: relative;
  background-color: #e8f2fe;
  border-radius: 5px;
  font-size: 10px;
  width: 55px;
  height: 30px;
  line-height: 30px;
  box-sizing: border-box;
  float: right;
  top: 50%;
  transform: translate3d(0, -50%, 0);
  user-select: none;
  cursor: pointer;
  margin-right: 5px;
}

.font-size-slider {
  display: block;
  position: relative;
  width: 85%;
  box-sizing: border-box;
  bottom: -45px;
  margin: 0 auto;
}

.note-title {
  position: relative;
  font-size: 25px;
  font-weight: bold;
  width: auto;
  height: 40px;
  line-height: 40px;
  box-sizing: border-box;
  float: left;
  top: 50%;
  transform: translate3d(0, -50%, 0);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.btn-to-top {
  position: absolute;
  background-color: #e8f2fe;
  top: 19px;
  right: 10px;
  border-radius: 5px;
  font-size: 10px;
  width: 55px;
  height: 30px;
  line-height: 30px;
  box-sizing: border-box;
  float: right;
  transform: translate3d(0, -50%, 0);
  user-select: none;
  cursor: pointer;
}
</style>
