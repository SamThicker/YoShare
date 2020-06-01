<template>
  <!--上传文件-->
  <div class="add-fav-wrap" v-show="show">
    <div class="add-fav">
      <div class="uploading" v-show="uploading">
        <el-progress
          class="uploadCircle"
          type="circle"
          :percentage="uploadPercentage"
          :color="colors"
        ></el-progress>
        <div class="message">{{ message }}</div>
      </div>
      <div class="add-fav-del" @click.stop="hide">&#215;</div>
      <label>标题</label>
      <el-input v-model="resourceName" placeholder="20字符内" maxlength="20">
      </el-input>
      <br />
      <br />

      <transition name="el-fade-in">
        <div class="file-preview" v-show="showPreview">
          <div>
            <div class="add-fav-del" @click.stop="delFile()">&#215;</div>
            <div class="preview-label">
              <i class="el-icon-document"></i>
            </div>
            <div class="file-name">{{ fileName }}</div>
          </div>
        </div>
      </transition>

      <el-upload
        class="file-upload"
        drag
        :action="''"
        :http-request="checkFileAndUpload"
        name="img"
        :headers="null"
        :show-file-list="false"
        :on-success="uploadSuccess"
        :on-error="uploadError"
        :before-upload="beforeUpload"
        :auto-upload="false"
        ref="upload"
        :multiple="true"
        :limit="1"
        :file-list="fileList"
        :on-change="onChange"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">
          只能上传单个文件，且不超过200MB
        </div>
      </el-upload>
      <br />
      <br />
      <div class="create-introduction">
        <label>描述</label>
        <el-input
          v-model="resourceIntroduction"
          type="textarea"
          :rows="5"
          placeholder="请输入内容，100字符内"
          maxlength="100"
        >
        </el-input>
      </div>
      <br />
      <el-button type="primary" @click="addFileRes()">创建</el-button>
    </div>
  </div>
</template>

<script>
import { hashFile } from "../../static/utils/fileUtil";
import { doUpload, readyForUpload, tryUpload } from "../api/GeneralFile";

export default {
  name: "FileUpload",
  props: {
    hideCallBack: Function,
    value: Boolean,
    destClassis: Object
  },
  mounted() {},
  data() {
    return {
      show: this.value,
      resourceName: "",
      resourceIntroduction: "",
      fileList: [],
      showPreview: false,
      fileName: "",
      uploading: false,
      currentFile: {
        type: null,
        content: null
      },
      currentFileInfo: null,
      uploadPercentage: 0,
      colors: [
        { color: "#f56c6c", percentage: 20 },
        { color: "#e6a23c", percentage: 40 },
        { color: "#6f7ad3", percentage: 60 },
        { color: "#1989fa", percentage: 80 },
        { color: "#5cb87a", percentage: 95 },
        { color: "#57b845", percentage: 100 }
      ],
      message: ""
    };
  },
  computed: {
    userId: function() {
      return this.$store.state.user.info.id;
    }
  },
  watch: {
    userId: function() {},
    value: function(bool) {
      this.show = bool;
      if (bool) {
        document.addEventListener("click", this.clickListener);
      }
    }
  },
  methods: {
    uploadSuccess: function() {
      this.createRes = false;
      this.uploading = false;
      this.refresh = true;
      this.delFile();
      let _this = this;
      setTimeout(function() {
        _this.refresh = false;
      }, 50);
    },
    uploadError: function() {
      this.uploading = false;
    },
    beforeUpload: function() {},
    checkFileAndUpload: function(data) {
      this.uploadPercentage = 0;
      this.uploading = true;
      let file = data.file;
      this.message = "正在解析文件...";
      hashFile(file, this.upload);
    },
    getId() {
      if (this.$route.params.groupId) {
        return this.$route.params.groupId;
      } else {
        return this.userId;
      }
    },
    getOption() {
      if (this.$route.params.groupId) {
        return "GROUP";
      } else {
        return "MEMBER";
      }
    },
    upload: function(md5, file) {
      //创建 formData 对象
      let formData = new FormData();
      // 向 formData 对象中添加文件
      formData.append("resTitle", this.resourceName);
      formData.append("resDescription", this.resourceIntroduction);
      formData.append("resClassification", this.destClassis.id);
      formData.append("fileName", file.name);
      formData.append("fileMd5", md5);
      let _this = this;
      tryUpload(formData, this.getId(), this.getOption())
        .then(function() {
          _this.uploadPercentage = 100;
          _this.$elementMessage("文件秒传成功", "success", 1000);
          _this.uploadSuccess();
        })
        .catch(function() {
          readyForUpload(formData, _this.getId(), _this.getOption())
            .then(function(res) {
              let fileData = new FormData();
              fileData.append("md5", md5);
              fileData.append("name", file.name);
              fileData.append("cursor", res.data);
              fileData.append("size", file.size);
              let totalNum = Math.ceil(parseInt(file.size) / (1024 * 1024 * 5));
              fileData.append("totalNum", totalNum);
              _this.uploadPercentage = (2 / (6 + totalNum)).toFixed(2) * 100;
              _this.uploadPart(fileData, 3, file);
            })
            .catch(err => {
              _this.uploadError();
              console.info(err);
              _this.$elementMessage(err.message, "error", 1000);
            });
        });
    },
    hide() {
      this.show = false;
      this.$emit("input", this.show);
      document.removeEventListener("click", this.clickListener);
    },
    uploadPart(formData, retry, file) {
      this.message =
        "正在上传 " +
        "(" +
        (parseInt(formData.get("cursor")) + 1) +
        "/" +
        formData.get("totalNum") +
        ")...";
      //计算文件切片总数
      let fileSize = parseInt(formData.get("size"));
      let bufferLength = 1024 * 1024 * 5;
      let totalNum = formData.get("totalNum");
      //计算开始的位置
      let start = formData.get("cursor") * bufferLength;
      let end = start + bufferLength;
      if (end > fileSize) end = fileSize;
      //切割文件
      var chunk = file.slice(start, end);
      let _this = this;
      formData.set("file", chunk);
      let cursor = parseInt(formData.get("cursor"));
      let percent = ((2 + cursor) / (parseInt(totalNum) + 6)).toFixed(2);
      _this.uploadPercentage = parseInt((percent * 100).toFixed(0));
      doUpload(formData, this.getId(), this.getOption())
        .then(function() {
          let cursor = parseInt(formData.get("cursor"));
          formData.set("cursor", cursor + 1);
          if (parseInt(formData.get("cursor")) < totalNum) {
            _this.uploadPart(formData, retry, file);
          } else {
            _this.message = "后台正在处理...";
            setTimeout(function() {
              _this.uploadPercentage = 100;
              setTimeout(function() {
                _this.uploading = false;
                _this.uploadPercentage = 0;
              }, 1000);
            }, 3000);
            _this.delFile();
            _this.refreshData();
          }
        })
        .catch(function() {
          retry--;
          if (retry > 0) {
            _this.uploadPart(formData, retry, file);
          } else {
            _this.uploading = false;
            _this.uploadPercentage = 0;
            _this.delFile();
          }
        });
    },
    clickListener(e) {
      let box = document.getElementsByClassName("add-fav")[0];
      if (box.contains(e.target)) return;
      this.hide();
    },
    //上传文件
    addFileRes() {
      this.$refs.upload.submit();
    },
    onChange(file, fileList) {
      if (fileList.length > 0) {
        this.showPreview = true;
        this.fileName = fileList[0].name;
        if (this.resourceName === "")
          this.resourceName = this.fileName.substring(0, 20);
      } else {
        this.showPreview = false;
      }
    },
    delFile() {
      this.resourceName = "";
      this.fileName = "";
      this.$refs.upload.clearFiles();
      this.showPreview = false;
    }
  }
};
</script>

<style scoped>
.resource-panel-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  display: -webkit-box;
}

.resource-panel {
  height: 100%;
  width: 500px;
}

.resource-content {
  height: 100%;
  flex: 1;
}

.add-fav-wrap {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1000;
  background-color: rgba(255, 255, 255, 0.8);
}

.add-fav {
  display: block;
  position: relative;
  top: 50%;
  left: 50%;
  transform: translate3d(-50%, -50%, 0);
  background-color: #e8f2fe;
  width: 360px;
  height: 580px;
  border-radius: 15px;
  padding: 20px;
  box-shadow: 1px 4px 10px 2px #ccc;
  -webkit-box-shadow: 1px 4px 10px 2px #ccc;
}

.add-fav-del {
  font-size: 30px;
  line-height: 30px;
  box-sizing: border-box;
  position: absolute;
  background-color: #eee;
  width: 30px;
  height: 30px;
  top: -15px;
  right: -15px;
  border-radius: 100%;
  border: 1px solid #fff;
  cursor: pointer;
  z-index: 100;
}

.file-preview {
  width: 360px;
  height: 180px;
  position: absolute;
  border-radius: 5px;
  z-index: 100;
  background-color: #ffffff;
  color: #b3b8c1;
}

.preview-label {
  font-size: 75px;
  position: relative;
  top: 30px;
}

.file-name {
  font-size: 13px;
  position: relative;
  top: 30px;
  overflow: auto;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.resource-content {
  width: 100%;
  height: 100%;
}

.uploading {
  position: absolute;
  width: 360px;
  height: 580px;
  z-index: 1000;
  background-color: rgba(255, 255, 255, 0.9);
}

.uploadCircle {
  position: relative;
  top: 50%;
  transform: translate3d(0, -50%, 0);
}

.message {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate3d(-50%, calc(-50% + 90px), 0);
  font-size: 15px;
}
</style>
