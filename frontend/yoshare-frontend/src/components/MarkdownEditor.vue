<template>
  <div style="width: 100%">
    <mavon-editor
      v-model="content"
      ref="md"
      @change="change"
      style="height: 100%;"
      @imgAdd="handleEditorImgAdd"
      @imgDel="handleEditorImgDel"
      :ishljs="true"
      :editable="editable"
    />
  </div>
</template>

<script>
// 导入组件 及 组件样式
import { mavonEditor } from "mavon-editor";
import "mavon-editor/dist/css/index.css";
import { getBlogFilePutUrl, uploadFile } from "../api/file";
import "element-ui/lib/theme-chalk/index.css";

export default {
  name: "MarkdownEditor",
  props: {
    /*编辑器的内容*/
    value: {
      type: String
    },
    containerHeight: Number,
    editable: Boolean
  },
  watch: {
    value: function() {
      this.content = this.value;
    },
    content: function() {
      //内容改变事件
      this.$emit("input", this.content);
    },
    containerHeight: function(val) {
      document.querySelector(".v-note-panel").style.height = val + "px";
    }
  },
  // 注册
  components: {
    mavonEditor
  },
  data() {
    return {
      content: this.value, // 输入的markdown
      html: "", // 及时转的html
      imageList: [],
      imageNum: 0
    };
  },
  methods: {
    // 所有操作都会被解析重新渲染
    change(value, render) {
      // render 为 markdown 解析后的结果[html]
      this.html = render;
    },
    // 提交
    submit() {
      console.log(this.content);
      console.log(this.html);
    },
    //添加图片
    upLoadPic(file) {
      let url;
      let userId = this.$store.state.user.info.id;
      return getBlogFilePutUrl(userId, file.name)
        .then(function(res) {
          let putUrl = res.data;
          return uploadFile(putUrl, file).then(function(res) {
            // 获取图片地址
            url = "http://localhost:" + res.config.url;
            url = url.replace(url.match("\\?.*"), "");
            return Promise.resolve(url);
          });
        })
        .catch(function(err) {
          console.info("error:" + err);
        });
    },
    //添加图片
    async handleEditorImgAdd(pos, file) {
      let url = await this.upLoadPic(file);
      let name = file.name;
      if (name.includes("-")) {
        name = name.replace(/-/g, "");
      }
      let content = this.content;
      // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)  这里是必须要有的
      if (content.includes(name)) {
        let oStr = `(${pos})`;
        let nStr = `(${url})`;
        let index = content.indexOf(oStr);
        let str = content.replace(oStr, "");
        let insertStr = (soure, start, newStr) => {
          return soure.slice(0, start) + newStr + soure.slice(start);
        };
        this.content = insertStr(str, index, nStr);
        //     // }
      } else {
        this.$Message.error("出错辣~");
      }
      // });
    },
    //删除图片
    handleEditorImgDel(pos) {
      delete this.imgFile[pos];
    }
  },
  mounted() {}
};
</script>

<style>
/*.v-note-panel {*/
/*  overflow: hidden!important;*/
/*  background-color: #ff6464;*/
/*  height: 300px!important;*/
/*}*/
/*.v-note-edit.divarea-wrapper.scroll-style.transition {*/
/*  overflow: scroll!important;*/
/*  background-color: #569ffb!important;*/
/*  !*height: 500px!important;*!*/
/*  width: 0;*/
/*}*/
</style>
