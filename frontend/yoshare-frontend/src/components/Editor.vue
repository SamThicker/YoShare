<template>
  <div class="editor editor-body quill-editor-wrapper">
    <!-- 图片上传组件辅助 -->
    <el-upload
      accept="image/jpeg,image/gif,image/png"
      class="avator-upload"
      :action="serverUrl"
      :http-request="doUpload"
      name="img"
      :headers="header"
      :show-file-list="false"
      :on-success="uploadSuccess"
      :on-error="uploadError"
      :before-upload="beforeUpload"
      style="display: none"
    >
    </el-upload>
    <quill-editor
      class="editor"
      v-model="content"
      ref="myQuillEditor"
      :options="editorOption"
      @blur="onEditorBlur($event)"
      @focus="onEditorFocus($event)"
      @change="onEditorChange($event)"
    ></quill-editor>
  </div>
</template>

<script>
// 工具栏配置
import { addQuillTitle } from "../../static/utils/quill-title";

const toolbarOptions = [
  ["bold", "italic", "underline", "strike"], // 加粗 斜体 下划线 删除线
  ["blockquote", "code-block"], // 引用  代码块
  [{ header: 1 }, { header: 2 }], // 1、2 级标题
  [{ list: "ordered" }, { list: "bullet" }], // 有序、无序列表
  [{ script: "sub" }, { script: "super" }], // 上标/下标
  [{ indent: "-1" }, { indent: "+1" }], // 缩进
  [{ direction: "rtl" }], // 文本方向
  [{ size: ["small", false, "large", "huge"] }], // 字体大小
  [{ header: [1, 2, 3, 4, 5, 6, false] }], // 标题
  [{ color: [] }, { background: [] }], // 字体颜色、字体背景颜色
  [{ font: [] }], // 字体种类
  [{ align: [] }], // 对齐方式
  ["clean"], // 清除文本格式
  [/*"link",*/ "image", "video"] // 链接、图片、视频
];

import { quillEditor } from "vue-quill-editor";
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";
import "highlight.js/styles/idea.css";
import hljs from "highlight.js";
import { getUploadUrl, uploadFile } from "../api/file";

export default {
  props: {
    /*编辑器的内容*/
    value: {
      type: String
    },
    /*图片大小*/
    maxSize: {
      type: Number,
      default: 5000 //kb
    },
    containerHeight: Number,
    enable: Boolean
  },

  mounted() {
    console.info("afadf");
    console.info(this.content)
;    this.$refs.myQuillEditor.quill.enable(this.enable);
    addQuillTitle();
  },

  components: {
    quillEditor
  },
  computed: {},
  watch: {
    containerHeight: function(val) {
      document.querySelector(".ql-editor").style.height = val + "px";
    },
    value: function(val) {
      this.content = val;
    },
    enable: function(val) {
      this.$refs.myQuillEditor.quill.enable(val);
      // this.$refs.myQuillEditor.quill.blur();
      // this.$nextTick(function() {
      //   this.$refs.myQuillEditor.quill.enable(val);
      //   this.$refs.myQuillEditor.quill.blur();
      // });
    }
  },
  data() {
    return {
      content: this.value,
      quillUpdateImg: false, // 根据图片上传状态来确定是否显示loading动画，刚开始是false,不显示
      editorOption: {
        theme: "snow", // or 'bubble'
        placeholder: "请开始你的表演",
        modules: {
          toolbar: {
            container: toolbarOptions,
            // container: "#toolbar",
            handlers: {
              image: function(value) {
                if (value) {
                  // 触发input框选择图片文件
                  document.querySelector(".el-upload__input").click();
                } else {
                  this.quill.format("image", false);
                }
              },
              link: function(value) {
                if (value) {
                  let href = prompt("请输入url");
                  this.quill.format("link", href);
                } else {
                  this.quill.format("link", false);
                }
              }
            }
          },
          syntax: {
            highlight: text => {
              return hljs.highlightAuto(text).value; // 这里就是代码高亮需要配置的地方
            }
          }
        }
      },
      serverUrl: "/v1/blog/imgUpload", // 这里写你要上传的图片服务器地址
      header: {
        // token: sessionStorage.token
      }, // 有的图片服务器要求请求头需要有token
      imageList: [],
      imageNum: 0
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
      this.$emit("input", this.content);
    },

    // 富文本图片上传前
    beforeUpload(file) {
      const isIMAGE = file.type === "image/jpeg" || "image/gif" || "image/png";
      const isLt1M = file.size / 1024 / 1024 < 10;

      if (!isIMAGE) {
        this.$message.error("上传文件只能是图片格式!");
      }
      if (!isLt1M) {
        this.$message.error("上传文件大小不能超过 10MB!");
      }
      // 显示loading动画
      this.quillUpdateImg = true;
      return isIMAGE && isLt1M;
    },

    uploadSuccess(res) {
      // res为图片服务器返回的数据
      // 获取富文本组件实例
      let quill = this.$refs.myQuillEditor.quill;
      // 如果上传成功
      if (res.status === 200) {
        // 获取图片地址
        let url = "http://localhost:" + res.config.url;
        url = url.replace(url.match("\\?.*"), "");
        let image = {
          index: this.imageNum,
          src: url
        };
        this.imageList[this.imageNum] = image;
        this.imageNum++;
        // 获取光标所在位置
        let pos = quill.getSelection().index;
        // 插入图片  res.url为服务器返回的图片地址
        quill.insertEmbed(pos, "image", url);
        // 调整光标到最后
        quill.setSelection(pos + 1);
      } else {
        this.$message.error("图片插入失败");
      }
      // loading动画消失
      this.quillUpdateImg = false;
    },
    // 富文本图片上传失败
    uploadError() {
      // loading动画消失
      this.quillUpdateImg = false;
      this.$message.error("图片插入失败");
    },
    doUpload(content) {
      let _this = this;
      getUploadUrl(content.file.name)
        .then(function(res) {
          uploadFile(res.data, content.file).then(function(res) {
            _this.uploadSuccess(res);
          });
        })
        .catch(function(err) {
          console.info("err:" + JSON.stringify(err));
          _this.uploadError(err);
        });
    }
  }
};
</script>

<style>
pre,
code {
  font-family: Consolas;
}

.quill-editor-wrapper {
  box-shadow: rgba(0, 0, 0, 0.1) 0 2px 12px 0;
  height: 100%;
}

.editor-body {
  -webkit-text-size-adjust: 100%;
  /*line-height: 1.5;*/
  color: #24292e;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial,
    sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
  font-size: 16px;
  word-wrap: break-word;
  line-height: normal !important;
  height: 100%;
  width: 100%;
}

.ql-container.ql-snow {
  height: 100% !important;
}

.ql-editor.ql-blank {
  overflow-y: scroll;
}

.ql-container.ql-snow {
  box-sizing: border-box;
  height: calc(100% - 65px);
}

.ql-editor.ql-blank,
.ql-container.ql-snow,
.ql-toolbar.ql-snow {
  border: none;
}

.ql-toolbar.ql-snow {
  border-bottom: 1px solid #eff4fc;
}

.ql-snow .ql-tooltip[data-mode="link"]::before {
  content: "请输入链接地址:";
}
.ql-snow .ql-tooltip.ql-editing a.ql-action::after {
  border-right: 0;
  content: "保存";
  padding-right: 0;
}

.ql-snow .ql-tooltip[data-mode="video"]::before {
  content: "请输入视频地址:";
}

.ql-snow .ql-picker.ql-size .ql-picker-label::before,
.ql-snow .ql-picker.ql-size .ql-picker-item::before {
  content: "14px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="small"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="small"]::before {
  content: "10px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="large"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="large"]::before {
  content: "18px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="huge"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="huge"]::before {
  content: "32px";
}

.ql-snow .ql-picker.ql-header .ql-picker-label::before,
.ql-snow .ql-picker.ql-header .ql-picker-item::before {
  content: "文本";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before {
  content: "标题1";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before {
  content: "标题2";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before {
  content: "标题3";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before {
  content: "标题4";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before {
  content: "标题5";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before {
  content: "标题6";
}

.ql-snow .ql-picker.ql-font .ql-picker-label::before,
.ql-snow .ql-picker.ql-font .ql-picker-item::before {
  content: "标准字体";
}
.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="serif"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="serif"]::before {
  content: "衬线字体";
}
.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="monospace"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="monospace"]::before {
  content: "等宽字体";
}

.ql-container{
  overflow-y: auto;
  height: 8rem!important;
}

/*滚动条整体样式*/
.ql-container ::-webkit-scrollbar{
  width: 10px;/*竖向滚动条的宽度*/
  height: 10px;/*横向滚动条的高度*/
}
.ql-container ::-webkit-scrollbar-thumb{/*滚动条里面的小方块*/
  background: #666666;
  border-radius: 5px;
}
.ql-container ::-webkit-scrollbar-track{/*滚动条轨道的样式*/
  background: #ccc;
  border-radius: 5px;
}
</style>
