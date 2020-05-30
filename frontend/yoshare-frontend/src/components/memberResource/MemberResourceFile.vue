<template>
  <div class="resource-panel-wrap">
    <resource-panel
      class="resource-panel"
      :type="'FILE'"
      :previewItemCallback="previewItemCallback"
      :classificationsCallback="classificationsCallBack"
      :refresh="refresh"
    ></resource-panel>

    <div class="resource-content">
      <file-view :file-info="currentFileInfo"></file-view>
    </div>

    <!--上传文件-->
    <div class="add-fav-wrap" v-show="createRes">
      <div class="add-fav">
        <div class="uploading" v-show="uploading">
          <el-progress type="circle" :percentage="25"></el-progress>
          <el-progress type="circle" :percentage="100" status="success"></el-progress>
        </div>
        <div class="add-fav-del" @click.stop="createRes = false">&#215;</div>
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
  </div>
</template>

<script>
import ResourcePanel from "@/components/ResourcePanel.vue";
import { delResourceNote } from "@/api/resource";
import {
  getFileInfo,
  uploadExistFileToServer,
  uploadFileToServer,
  uploadMultipartFile
} from "../../api/file";
// import SparkMD5 from "spark-md5";
import { hashFile } from "../../../static/utils/fileUtil";
import FileView from "../FileView";
export default {
  name: "CategoryResource",
  components: { FileView, ResourcePanel },
  data() {
    return {
      noteSearch: "",
      deleteLoading: false,
      noteLoading: false,
      note: null,
      toTop: false,
      classifications: [],
      previewItemCallback: {
        click: this.fileClicked,
        star: this.itemStarClicked,
        unstar: this.itemUnstarClicked,
        share: this.itemShareClicked,
        del: null
      },
      classificationsCallBack: {
        click: null,
        addRes: this.addFileResDialog
      },
      refresh: false,
      createRes: false,
      resourceName: "",
      resourceIntroduction: "",
      resourceUrl: "",
      fileList: [],
      showPreview: false,
      fileName: "",
      uploading: false,
      currentFile: {
        type: null,
        content: null
      },
      currentFileInfo: null
    };
  },
  computed: {
    userId: function() {
      return this.$store.state.user.info.id;
    }
  },
  watch: {
    userId: function() {},
    note: function() {}
  },
  methods: {
    //资源预览项点击事件
    fileClicked: function(resource) {
      let _this = this;
      getFileInfo(resource.resourceRef)
        .then(function(res) {
          _this.currentFileInfo = res.data;
          // _this.currentFile.type = res.headers["content-type"];
          // _this.currentFile.content = res.data;
        })
        .catch();
    },
    itemStarClicked: function() {},
    itemUnstarClicked: function() {},
    itemShareClicked: function() {},
    itemDelClicked(resource) {
      let _this = this;
      this.$confirm("此操作将永久删除该笔记, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          _this.deleteLoading = true;
          delResourceNote(_this.$store.state.user.info.id, resource)
            .then(function() {
              let index = _this.resources.indexOf(resource);
              if (index > -1) {
                _this.resources.splice(index, 1);
              }
              _this.deleteLoading = false;
            })
            .catch(() => {
              _this.$elementMessage("操作失败，请重试", "error", 1500);
              _this.deleteLoading = false;
            });
        })
        .catch(() => {
          _this.deleteLoading = false;
        });
    },
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
      this.uploading = true;
      let file = data.file;
      hashFile(file, this.doUpload);
    },
    doUpload: function(md5, file) {
      //创建 formData 对象
      let formData = new FormData();
      // 向 formData 对象中添加文件
      formData.append("resTitle", this.resourceName);
      formData.append("resDescription", this.resourceIntroduction);
      formData.append("resClassification", this.destClassis.id);
      formData.append("fileName", file.name);
      formData.append("fileMd5", md5);
      let _this = this;
      uploadExistFileToServer(this.userId, formData)
        .then(function() {
          _this.$elementMessage("文件秒传成功", "success", 1000);
          _this.uploadSuccess();
        })
        .catch(function() {
          uploadFileToServer(_this.userId, formData)
            .then(function(res) {
              let fileData = new FormData();
              fileData.append("md5", md5);
              fileData.append("name", file.name);
              fileData.append("cursor", res.data);
              fileData.append("size", file.size);
              let totalNum = Math.ceil(parseInt(file.size) / (1024 * 1024 * 5));
              fileData.append("totalNum", totalNum);
              _this.uploadPart(fileData, 3, file);
            })
            .catch(err => {
              _this.uploadError();
              console.info(err);
              _this.$elementMessage(err.message, "error", 1000);
            });
        });
    },
    uploadPart(formData, retry, file) {
      console.info(
        "uploading " +
          "(" +
          (parseInt(formData.get("cursor")) + 1) +
          "/" +
          formData.get("totalNum") +
          ")"
      );
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
      uploadMultipartFile(formData)
        .then(function() {
          formData.set("cursor", parseInt(formData.get("cursor")) + 1);
          if (parseInt(formData.get("cursor")) < totalNum) {
            _this.uploadPart(formData, retry, file);
          } else {
            _this.uploading = false;
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
            _this.delFile();
          }
        });
    },
    clickListener(e) {
      let box = document.getElementsByClassName("add-fav")[0];
      if (box.contains(e.target)) return;
      this.createRes = false;
      document.removeEventListener("click", this.clickListener);
    },
    addFileResDialog: function(classis) {
      this.destClassis = classis;
      this.createRes = true;
      document.addEventListener("click", this.clickListener);
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
    },
    refreshData() {
      let _this = this;
      this.refresh = true;
      setTimeout(function() {
        _this.refresh = false;
      }, 50);
    }

    // doUpload: function(md5, file) {
    //   console.info("md5:" + md5);
    //   //创建 formData 对象
    //   let formData = new FormData();
    //   // 向 formData 对象中添加文件
    //   formData.append("title", this.resourceName);
    //   formData.append("description", this.resourceIntroduction);
    //   formData.append("classis", this.destClassis.id);
    //   formData.append("name", file.name);
    //   formData.append("hash", md5);
    //   let _this = this;
    //   uploadExistFileToServer(this.userId, formData)
    //     .then(function() {
    //       _this.$elementMessage("文件秒传成功", "success", 1000);
    //       _this.uploadSuccess();
    //     })
    //     .catch(function() {
    //       formData.append("file", file);
    //       uploadFileToServer(_this.userId, formData)
    //         .then(function() {
    //           _this.$elementMessage("文件上传成功", "success", 1000);
    //           _this.uploadSuccess();
    //         })
    //         .catch(err => {
    //           _this.uploadError();
    //           console.info(err);
    //           _this.$elementMessage(err.message, "error", 1000);
    //         });
    //     });
    // },
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
  z-index: 10;
  background-color: rgba(255, 255, 255, 0.8);
}
</style>
