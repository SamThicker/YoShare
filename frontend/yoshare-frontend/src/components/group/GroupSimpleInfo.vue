<template>
  <div class="group-simple-info-wrap">
    <el-form ref="form" label-width="130px" :label-position="formLabelAlign">
      <el-form-item label="头像">
        <avatar :options="options" :funcs="funcs"></avatar>
      </el-form-item>
      <el-form-item label="小组ID"></el-form-item>
      <el-form-item label="创建时间"> </el-form-item>
      <el-form-item label="小组名">
        <el-input></el-input>
      </el-form-item>
      <el-form-item label="简介">
        <el-input type="textarea"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary">保存</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { refreshAvatarNew } from "../../api/user";
import Avatar from "@/components/Avatar";
import { getAvatarUploadUrl } from "@/api/group";
import { uploadFile } from "../../api/file";
import { deepClone } from "../../../static/utils/deepClone";
export default {
  name: "GroupSimpleInfo",
  components: { Avatar },
  mounted() {
    console.info("mounted");
    if (this.groups == null || this.groups.length <= 0) return;
    this.setCurrentGroup();
  },
  data() {
    return {
      deep: true,
      formLabelAlign: "left",
      options: {
        src: this.icon,
        size: 100,
        alt: "http://localhost/static/icon/DEFAULT.png",
        accept: "image/jpeg,image/png",
        serverUrl: "",
        header: null,
        reload: false
      },
      funcs: {
        doUpload: this.uploadAvatar,
        uploadSuccess: this.uploadSuccess,
        uploadError: this.uploadError,
        beforeUpload: this.beforeUpload
      },
      currentGroupInfo: null
    };
  },
  watch: {
    deep: true,
    path: function() {},
    groups: function() {
      this.setCurrentGroup();
    },
    currentGroupId: function() {
      this.setCurrentGroup();
    },
    currentGroupInfo: function() {
      this.options.src = this.icon;
    }
  },
  computed: {
    path: function() {
      return this.$route.params.routerId;
    },
    currentGroupId: function() {
      return this.$route.params.groupId;
    },
    icon: function() {
      let icon = this.currentGroupInfo.icon;
      if (icon) {
        return "http://localhost" + icon;
      }
      return "http://localhost/static/icon/DEFAULT.png";
    },
    groups: function() {
      return this.$store.state.user.allGroups;
    }
  },
  methods: {
    setCurrentGroup: function() {
      let _this = this;
      let currentGroup = this.groups.filter(group => {
        return parseInt(group.id) === parseInt(_this.currentGroupId);
      });
      this.currentGroupInfo = deepClone(currentGroup[0]);
      this.options.src = this.currentGroupInfo.icon;
    },
    uploadAvatar(content) {
      let _this = this;
      let userId = this.$store.state.user.info.id;
      let file = content.file;
      let type = file.type.replace("image/", "");
      let groupId = this.currentGroupId;
      getAvatarUploadUrl(userId, groupId, type)
        .then(function(res) {
          let url = res.data;
          uploadFile(url, file).then(function(res) {
            _this.uploadSuccess(res);
          });
        })
        .catch(function(err) {
          console.info("err:" + JSON.stringify(err));
          _this.uploadError(err);
        });
    },
    uploadSuccess: function() {
      this.$store.dispatch("getAllGroups", this.$store.state.user.info.id);
      refreshAvatarNew(this.currentGroupInfo.icon).catch();
      this.options.reload = true;
      this.options.reload = false;
    },
    uploadError: function() {},
    beforeUpload: function() {}
  }
};
</script>

<style scoped>
.group-simple-info-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  flex: 1;
  padding: 20px;
  box-sizing: border-box;
  max-width: 700px;
}
</style>
