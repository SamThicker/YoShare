<template>
  <div class="group-category-note-wrap">
    <group-resource-panel
      class="resource-panel"
      :type="'FILE'"
      :previewItemCallback="previewItemCallback"
      :classificationsCallback="classificationsCallBack"
      :refresh="refresh"
    ></group-resource-panel>

    <div class="resource-content">
      <file-view :file-info="currentFileInfo"></file-view>
    </div>

    <!--上传文件-->
    <div class="add-fav-wrap" v-show="createRes">
      <div class="add-fav" v-loading="uploading">
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
import { delResourceNote } from "@/api/resource";
import {
  getFileInfo,
  uploadExistFileToServer,
  uploadFileToServer
} from "../../api/groupFile";
// import SparkMD5 from "spark-md5";
import { hashFile } from "../../../static/utils/fileUtil";
import GroupResourcePanel from "./GroupResourcePanel";
import FileView from "../FileView";
export default {
  name: "CategoryResource",
  components: { FileView, GroupResourcePanel },
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
    },
    groupId: function() {
      return this.$route.params.groupId;
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
      getFileInfo(_this.groupId, resource.resourceRef)
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
      uploadExistFileToServer(this.groupId, formData)
        .then(function() {
          _this.$elementMessage("文件秒传成功", "success", 1000);
          _this.uploadSuccess();
        })
        .catch(function() {
          formData.append("file", file);
          uploadFileToServer(_this.groupId, formData)
            .then(function() {
              _this.$elementMessage("文件上传成功", "success", 1000);
              _this.uploadSuccess();
            })
            .catch(err => {
              _this.uploadError();
              console.info(err);
              _this.$elementMessage(err.message, "error", 1000);
            });
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
    },
  }
};
</script>

<style scoped>
.group-category-note-wrap {
  width: 100%;
  height: 100%;
  position: relative;
  display: flex;
}

.resource-panel {
  height: 100%;
  width: 500px;
}

.resource-content {
  height: 100%;
  flex: 1;
  overflow: hidden;
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
  height: auto;
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


</style>
