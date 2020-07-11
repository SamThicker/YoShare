<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <!--  workbench  -->
  <div class="workbench" v-loading="onSaveLoading">
    <!--  菜单  -->
    <div class="workbench-menu">
      <div class="back-link" @click="backHome()">
        <i class="el-icon-arrow-left"></i>
        <span class="back-link-label">Back</span>
      </div>
      <el-input
        class="title"
        placeholder="请输入标题"
        v-model="title"
      ></el-input>
      <div class="note-edit">
        <div class="setting-btn" @click.stop="showSettingBar">
          <i class="el-icon-setting"></i>
        </div>
        <el-button
          type="primary"
          class="btn-save"
          @click.stop="showSaveOption()"
          >保存</el-button
        >
        <el-button type="primary" class="btn-publish" v-show="isGroup()"
          >发布</el-button
        >
      </div>

      <!-- 设置面板 -->
      <transition name="el-fade-in">
        <div class="setting-bar" v-show="settingBar">
          <div class="editor-switcher setting-option">
            <span class="setting-label">编辑器</span>
            <div class="switcher-label-wrapper">
              <span class="switcher-label">Q M</span>
            </div>
            <el-switch
              v-bind:disabled="!!content"
              class="switcher-btn"
              v-model="isMavon"
              active-color="#13ce66"
              inactive-color="#ff4949"
            >
            </el-switch>
          </div>
          <div class="editor-switcher setting-option">
            <span class="setting-label">时光机</span>
            <div class="switcher-label-wrapper">
              <span class="switcher-label">隐 显</span>
            </div>
            <el-switch
              class="switcher-btn"
              v-model="timeline"
              active-color="#13ce66"
              inactive-color="#ff4949"
            >
            </el-switch>
          </div>
        </div>
      </transition>
    </div>

    <!-- 内容 -->
    <div class="workbench-content">
      <!--时光机-->
      <transition name="el-fade-in">
        <div class="timeline" v-show="timeline">
          <TimeLine
            :call-back="timeLineCallback"
            :items="items"
            :end-item="endItem"
          ></TimeLine>
        </div>
      </transition>

      <!-- 编辑器面板 -->
      <div class="editor-panel">
        <Editor
          :enable="markdownEditable"
          v-if="!isMavon"
          v-model="content"
          ref="myQuillEditor"
          :options="editorOption"
          @blur="onEditorBlur($event)"
          @focus="onEditorFocus($event)"
          @change="onEditorChange($event)"
        ></Editor>
        <MarkdownEditor
          v-else
          v-model="content"
          :editable="markdownEditable"
        ></MarkdownEditor>
      </div>
    </div>

    <!--保存选项-->
    <div class="save-option-panel" v-show="saveOption">
      <div class="save-option-bar">
        <div class="save-option-bar-close-btn" @click="saveOption = false">
          &#215;
        </div>
        <div>
          <label>标记</label>
          <el-input v-model="currentTag"></el-input>
        </div>
        <div
          style="text-align: left; margin-top: 15px; color:#a2adc1; font-size: 13px"
        >
          "标记"是指在时光机的时间线上的文字，可用于方便的检索笔记的历史记录。
        </div>
        <div class="btn-do-save">
          <el-button type="primary" @click="saveNote">确定</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import TimeLine from "../components/TimeLine";
import { getNoteForSelf, saveNote } from "../api/note";
import { formatDateTime } from "../../static/utils/dateUtil";
import { getGroupNote, saveGroupNote } from "../api/groupNote";
export default {
  name: "WorkBench",
  watch: {
    content: function() {
      console.info("c:"+this.content)
      // this.content = "";
      //  // this.content = val.replace(/<script>/gi, this.stringEncode("<script>"));
      //  this.content = val.replace(/<script>/gi, "哈哈");
      //  this.content = val.replace(/<[^>]+>/g,"哈哈");
      // // 替换字符串变量或者结束标签这样写
      //  this.content = val.replace(new RegExp('&ls;/script>','gi'), this.stringEncode("&lt;/script>"));
      //  this.content = val.replace("<script>","\\&lt;script&gt;");
      //
      //  //转义括号"()"
      //  this.content = val.replace(/\(/g,"%28").replace(/\)/g,"%29");
      //  //双引号
      //  this.content = val.replace("\"","&quot;")
      //  //单引号
      //  this.content = val.replace("'","&apos;")
      //  if (this.isMavon) this.content = val.replace("<img>","\\" +"&lt;img>")
    },
    $route: function() {
      this.getParams();
      this.getNote();
    },
    currentNote: function() {
      this.setData();
    }
  },
  components: { TimeLine },
  mounted() {
    let _this = this;
    this.timeLineCallback = function(id) {
      _this.timeLineItemClick(id);
    };
    this.getParams();
    this.getNote();
    this.setData();
  },
  destroyed() {
    window.onresize = null;
    document.removeEventListener("click", this.clickListener);
  },
  data() {
    return {
      title: "",
      content: "",
      editingBackup: "",
      editorOption: {},
      isMavon: true,
      timeLineCallback: null,
      items: [],
      endItem: {
        index: this.getCurrentTime(),
        content: "正在编辑...",
        id: null
      },
      settingBar: false,
      timeline: true,
      quillContainerHeight: 0,
      mdContainerHeight: 0,
      currentNote: null,
      userId: null,
      noteId: null,
      groupId: null,
      classId: null,
      saveOption: false,
      editing: true,
      markdownEditable: true,
      onSaveLoading: false,
      currentVersionId: "",
      currentTag: "",
      currentEditor: ""
    };
  },
  computed: {
    currentTime: function() {
      return this.getCurrentTime();
    }
  },
  methods: {
    stringEncode(str) {
      var div = document.createElement("div");
      if (div.innerText) {
        div.innerText = str;
      } else {
        div.textContent = str;
      }
      return div.innerHTML;
    },
    isGroup() {
      return !!this.groupId;
    },
    getCurrentTime() {
      let date = new Date();
      let month = date.getMonth() + 1;
      let today = date.getDate();
      if (month < 10) {
        month = "0" + month;
      }
      if (today < 10) {
        today = "0" + today;
      }
      return (
        date.getFullYear() +
        "-" +
        month +
        "-" +
        today +
        " " +
        date.getHours() +
        ":" +
        date.getMinutes() +
        ":" +
        date.getSeconds()
      );
    },
    backHome() {
      this.$router.back();
      // this.$router.push("/");
    },
    showSettingBar() {
      this.settingBar = !this.settingBar;
      if (this.settingBar) {
        document.addEventListener("click", this.clickListener);
      } else {
        document.removeEventListener("click", this.clickListener);
      }
    },
    clickListener(e) {
      let box = document.getElementsByClassName("setting-bar")[0];
      if (box.contains(e.target)) return;
      this.settingBar = false;
      document.removeEventListener("click", this.clickListener);
    },
    getParams() {
      this.userId = this.$route.params.userId;
      this.noteId = this.$route.params.noteId;
      this.groupId = this.$route.query.groupId;
      this.classId = this.$route.query.classId;
    },
    getNote() {
      let _this = this;
      if (!this.userId || !this.noteId) return;
      let groupId = this.$route.query.groupId;
      console.info(groupId);
      if (groupId) {
        getGroupNote(groupId, this.noteId)
          .then(function(res) {
            _this.currentNote = res.data;
            _this.currentNote.contents.forEach(content => {
              content.time = formatDateTime(content.time);
            });
          })
          .catch(function(err) {
            console.info("err:" + err);
          });
      } else {
        getNoteForSelf(this.userId, this.noteId)
          .then(function(res) {
            _this.currentNote = res.data;
            _this.currentNote.contents.forEach(content => {
              content.time = formatDateTime(content.time);
            });
          })
          .catch(function(err) {
            console.info("err:" + err);
          });
      }
    },
    setData() {
      if (!this.currentNote) return;
      let lastIndex = this.currentNote.contents.length - 1;
      this.isMavon = this.currentNote.contents[lastIndex].editor === "MAVON";
      this.currentEditor = this.isMavon;
      // this.this.quill.pasteHTML('<h3>add some title</h3>' + html)
      this.content = this.currentNote.contents[lastIndex].content;
      this.editingBackup = this.content;
      this.title = this.currentNote.title;
      let items = [];
      for (let content of this.currentNote.contents) {
        let item = {
          id: content.id,
          index: content.time,
          content: content.tag
        };
        items.push(item);
      }
      this.items = null;
      this.items = items;
    },
    //保存笔记，如果是新的笔记则新建笔记
    saveNote() {
      let content = {
        content: this.content,
        editor: this.isMavon ? "MAVON" : "QUILL",
        tag: this.currentTag
      };
      let note = {
        id: this.noteId,
        title: this.title,
        contents: [content],
        by: this.userId
      };
      let data = {};
      data.note = note;
      data.classId = this.classId;
      let _this = this;
      this.onSaveLoading = true;
      if (!this.groupId) {
        saveNote(this.userId, data)
          .then(function(res) {
            _this.$elementMessage("保存成功", "success", 1000);
            _this.onSaveLoading = false;
            if (!_this.$route.params.noteId) {
              let id = res.data.id;
              _this.$router.replace("/workBench/" + _this.userId + "/" + id);
            }
            _this.getNote();
            _this.setData();
          })
          .catch(function(err) {
            _this.$elementMessage("操作失败", "error", 1000);
            _this.onSaveLoading = false;
            console.info("er:" + err);
          });
      } else {
        saveGroupNote(this.groupId, data)
          .then(function(res) {
            _this.$elementMessage("保存成功", "success", 1000);
            _this.onSaveLoading = false;
            if (!_this.$route.params.noteId) {
              let id = res.data.id;
              let path = "/workBench/" + _this.userId + "/" + id;
              let query = {
                groupId: _this.groupId
              };
              _this.$router.replace({
                path: path,
                query: query
              });
            }
            _this.getNote();
            _this.setData();
          })
          .catch(function(err) {
            _this.$elementMessage("操作失败", "error", 1000);
            _this.onSaveLoading = false;
            console.info("er:" + err);
          });
      }
      this.saveOption = false;
    },
    //显示保存选项
    showSaveOption() {
      //如果当前笔记包含历史版本，则显示当前最新版本
      if (this.noteId) {
        this.timeLineItemClick("");
      }
      this.currentTag = this.noteId ? "更改" : "新建";
      this.editing = true;
      this.markdownEditable = true;
      this.saveOption = true;
      document.addEventListener("click", this.saveOptionBarListener);
    },
    saveOptionBarListener(e) {
      let el = document.querySelector(".save-option-bar");
      if (el.contains(e.target)) return;
      this.saveOption = false;
      document.removeEventListener("click", this.saveOptionBarListener);
    },
    //点击时间线更改内容
    timeLineItemClick(id) {
      //id不为空则表示点击历史记录
      if (id) {
        this.currentVersionId = id;
        this.markdownEditable = false;
        //如果当前是编辑状态，则备份内容
        if (this.editing) {
          this.editingBackup = this.content;
          this.editing = false;
        }
        let contents = this.currentNote.contents.filter(
          content => content.id === id
        );
        this.isMavon = contents[0].editor === "MAVON";
        this.content = contents[0].content;
        return;
      }
      //如果id为空，表示点击当前编辑状态
      //如果noteId为空，则表示新笔记，没有历史版本不需要备份
      if (this.noteId && this.currentVersionId != "") {
        this.content = this.editingBackup;
        this.isMavon = this.currentEditor;
        this.$mount();
      }
      this.currentVersionId = "";
      this.editing = true;
      this.markdownEditable = true;
    }
  }
};
</script>

<style>
html body {
  height: 100%;
}

/*总体*/
.workbench {
  display: flex;
  flex-direction: column;
  /*display: -webkit-box;*/
  /*flex-direction: column;*/
  height: 100%;
  /*height: 100vh;*/
}

/*菜单*/
.workbench-menu {
  width: 100%;
  height: 58px;
  border: 0 solid #f0f0f0;
  border-bottom-width: 1px;
  background-color: #f0f0f0;
  overflow: hidden;
  display: flex;
  flex: 0 0 auto;
}

/*返回链接*/
.back-link {
  width: 100px;
  height: 100%;
  position: relative;
  float: left;
  color: #7bb6fb;
  cursor: pointer;
}

/*箭头*/
.el-icon-arrow-left {
  line-height: 58px;
  height: 25px;
  font-size: 18px;
  font-weight: bold;
}

.back-link-label {
  line-height: 58px;
  font-size: 18px;
}

/*标题*/
.title {
  line-height: 58px;
  font-size: 20px;
  margin-left: 10px;
  margin-right: 10px;
  flex: 1;
}

/*笔记编辑菜单*/
.note-edit {
  position: relative;
  float: right;
  height: 100%;
  width: auto;
}

/*保存、发布*/
.btn-save,
.btn-publish {
  top: 8px;
  position: relative;
  float: right;
  margin-right: 20px;
}

/*设置相关*/
.setting-btn {
  position: relative;
  width: 50px;
  height: 100%;
  display: inline-block;
  float: right;
  right: 10px;
  cursor: pointer;
}

.el-icon-setting {
  font-size: 30px;
  line-height: 58px;
  height: 30px;
  color: #7bb6fb;
}

.setting-bar {
  position: absolute;
  z-index: 9999;
  top: 58px;
  right: 10px;
  height: auto;
  width: auto;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #fff;
  padding: 10px;
  box-shadow: 3px 5px 3px 0 #c1c1c1;
  display: flex;
  flex-wrap: wrap;
  max-width: 172px;
}

.setting-label {
  font-size: 10px;
}

.editor-switcher,
.setting-option {
  width: 75px;
  height: 75px;
  border-radius: 5px;
}

.setting-option:hover {
  background-color: #d1e7fe;
}

/*内容面板*/
.workbench-content {
  display: flex;
  /*flex: 1;*/
  /*height: 100%;*/

  flex-grow: 1;
  flex-shrink: 1;
  overflow: hidden;
}

.timeline {
  width: 180px;
  height: 100%;
  position: relative;
  float: left;
  box-sizing: border-box;
  display: flex;
  overflow: auto;
}

.editor-panel {
  width: 100%;
  display: flex;
  height: 100%;
  position: relative;
  float: left;
  flex: 1;
  min-width: 0;
  margin: auto;
  overflow: hidden;
}

.save-option-panel {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 10000;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.8);
}

.save-option-bar {
  position: relative;
  height: 250px;
  width: 400px;
  top: 50%;
  left: 50%;
  transform: translate3d(-50%, -50%, 0);
  background-color: #fff;
  border-radius: 15px;
  box-shadow: 1px 4px 10px 2px #ccc;
  -webkit-box-shadow: 1px 4px 10px 2px #ccc;
  padding: 20px;
  box-sizing: border-box;
}

.save-option-bar-close-btn {
  position: absolute;
  font-size: 22px;
  font-weight: bold;
  line-height: 18px;
  width: 20px;
  height: 20px;
  top: -10px;
  right: -10px;
  box-sizing: border-box;
  border-radius: 100%;
  background-color: #eee;
  border: 1px solid #fff;
  cursor: pointer;
}

.btn-do-save {
  position: absolute;
  bottom: 15px;
  left: 50%;
  transform: translate3d(-50%, 0, 0);
  width: 100px;
  height: 50px;
}

.ql-editor {
  height: 100vh;
}

.ql-container {
  overflow-y: auto;
  height: 8rem !important;
}

/*滚动条整体样式*/
.ql-container ::-webkit-scrollbar {
  width: 10px; /*竖向滚动条的宽度*/
  height: 10px; /*横向滚动条的高度*/
}
.ql-container ::-webkit-scrollbar-thumb {
  /*滚动条里面的小方块*/
  background: #666666;
  border-radius: 5px;
}
.ql-container ::-webkit-scrollbar-track {
  /*滚动条轨道的样式*/
  background: #ccc;
  border-radius: 5px;
}

body {
  margin: 0 !important;
}
</style>
