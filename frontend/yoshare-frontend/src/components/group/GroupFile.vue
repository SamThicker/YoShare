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

    <file-upload v-model="showUpload" :dest-classis="destClassis"></file-upload>

  </div>
</template>

<script>
import { delResourceNote } from "@/api/resource";
import { getFileInfo } from "../../api/groupFile";
import GroupResourcePanel from "./GroupResourcePanel";
import FileView from "../FileView";
import FileUpload from "../FileUpload";
export default {
  name: "CategoryResource",
  components: { FileUpload, FileView, GroupResourcePanel },
  data() {
    return {
      showUpload: false,
      destClassis: null,
      noteSearch: "",
      deleteLoading: false,
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
    addFileResDialog: function(classis) {
      this.destClassis = classis;
      this.showUpload = true;
    },
    //上传文件
    addFileRes() {
      this.$refs.upload.submit();
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
