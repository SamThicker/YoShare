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
// import xss from "xss";

export default {
  name: "MarkdownEditor",
  props: {
    /*编辑器的内容*/
    value: {
      type: String
    },
    editable: Boolean
  },
  watch: {
    value: function() {
      // let xss = require("xss");
      // this.content = xss(this.value);
    },
    content: function() {
      // this.content = this.value;
      //内容改变事件
      this.$emit("input", this.content);
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
      imageNum: 0,
      xssOption: {
        onTag: this.onTag
      }
    };
  },
  methods: {
    onTag(tag/*, html, options*/) {
      // tag是当前的标签名称，比如<a>标签，则tag的值是'a'
      // html是该标签的HTML，比如<a>标签，则html的值是'<a>'
      // options是一些附加的信息，具体如下：
      //   isWhite    boolean类型，表示该标签是否在白名单上
      //   isClosing  boolean类型，表示该标签是否为闭合标签，比如</a>时为true
      //   position        integer类型，表示当前标签在输出的结果中的起始位置
      //   sourcePosition  integer类型，表示当前标签在原HTML中的起始位置
      // 如果返回一个字符串，则当前标签将被替换为该字符串
      // 如果不返回任何值，则使用默认的处理方法：
      //   在白名单上：  通过onTagAttr来过滤属性，详见下文
      //   不在白名单上：通过onIgnoreTag指定，详见下文
      if (tag === "object") {
        console.info("sssssssssssssssss")
        return "哈哈哈哈哈哈"
      }
    },
    //所有操作都会被解析重新渲染
    change(value, render) {
      // let myxss = new xss.FilterXSS(this.xssOption);
      // // render 为 markdown 解析后的结果[html]
      // let filtered = myxss.process(render);
      // console.info("filtered:");
      // this.html = filtered;
      // console.info(this.html)
      // this.html = "";
      console.info(value);
      value = ""
      // render = ""
      console.info(render)
      render = ""
    },
    // 提交
    submit() {
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
            url = "http://" + window.location.host + ":" + res.config.url;
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
