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

    <file-upload v-model="showUpload" :dest-classis="destClassis"></file-upload>

  </div>
</template>

<script>
import ResourcePanel from "@/components/ResourcePanel.vue";
import { delResourceNote } from "@/api/resource";
import { getFileInfo } from "../../api/file";
import FileView from "../FileView";
import FileUpload from "../FileUpload";
export default {
  name: "CategoryResource",
  components: { FileUpload, FileView, ResourcePanel },
  data() {
    return {
      showUpload: false,
      noteSearch: "",
      deleteLoading: false,
      noteLoading: false,
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
      resourceName: "",
      resourceIntroduction: "",
      resourceUrl: "",
      fileList: [],
      showPreview: false,
      fileName: "",
      currentFile: {
        type: null,
        content: null
      },
      destClassis: null,
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
        })
        .catch();
    },
    itemStarClicked: function() {},
    itemUnstarClicked: function() {},
    itemShareClicked: function() {},
    itemDelClicked(resource) {
      let _this = this;
      this.$confirm("此操作将永久删除该文件, 是否继续?", "提示", {
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
    addFileResDialog: function(classis) {
      this.destClassis = classis;
      this.showUpload = true;
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
