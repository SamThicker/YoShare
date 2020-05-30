<template>
  <div class="follow-panel-wrap">
    follow
    <div class="follow-message"></div>

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

    <button>123</button>
  </div>
</template>

<script>
import { hashFile } from "../../static/utils/fileUtil";
import { uploadFileParts } from "../api/file";

export default {
  name: "Follow",
  data() {
    return {
      fileList: []
    };
  },
  methods: {
    checkFileAndUpload: function(data) {
      this.uploading = true;
      let file = data.file;
      hashFile(file, this.doUpload);
    },
    doUpload: function(md5, file) {
      console.info("md5:" + md5);
      //创建 formData 对象
      let formData = new FormData();
      // 向 formData 对象中添加文件
      formData.append("title", this.resourceName);
      formData.append("description", this.resourceIntroduction);
      formData.append("classis", this.destClassis.id);
      formData.append("name", file.name);
      formData.append("hash", md5);
      let _this = this;
      uploadFileParts(formData)
        .then(function() {
          _this.$elementMessage("文件秒传成功", "success", 1000);
          _this.uploadSuccess();
        })
        .catch(function() {
          formData.append("file", file);
          // uploadFileToServer(_this.userId, formData)
          //   .then(function() {
          //     _this.$elementMessage("文件上传成功", "success", 1000);
          //     _this.uploadSuccess();
          //   })
          //   .catch(err => {
          //     _this.uploadError();
          //     console.info(err);
          //     _this.$elementMessage(err.message, "error", 1000);
          //   });
        });
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
.follow-panel-wrap {
  position: relative;
  width: 100%;
  height: 100%;
}
</style>
