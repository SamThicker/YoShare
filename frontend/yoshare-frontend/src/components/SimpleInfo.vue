<template>
  <el-form
    ref="form"
    :model="info"
    label-width="130px"
    :label-position="formLabelAlign"
  >
    <el-form-item label="头像">
      <avatar :options="options" :funcs="funcs"></avatar>
    </el-form-item>
    <el-form-item label="用户ID">{{ info.id }} </el-form-item>
    <el-form-item label="注册时间">{{ info.registeredTime }} </el-form-item>
    <el-form-item label="用户名">{{ info.username }} </el-form-item>
    <el-form-item label="昵称">
      <el-input v-model="info.nickname"></el-input>
    </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="info.gender" class="info-gender">
        <el-col :span="8"><el-radio label="0">女生</el-radio></el-col>
        <el-col :span="8"><el-radio label="1">男生</el-radio></el-col>
        <el-col :span="8"><el-radio label="2">保密</el-radio></el-col>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="生日">
      <el-date-picker
        type="date"
        placeholder="2000-01-01"
        v-model="info.birthday"
        style="width: 100%;"
      ></el-date-picker>
    </el-form-item>
    <el-form-item label="地区">
      <v-distpicker
        @selected="onSelected"
        :province="select.province"
        :city="select.city"
        :area="select.area"
        v-if="update"
      ></v-distpicker>
    </el-form-item>
    <el-form-item label="简介">
      <el-input type="textarea" v-model="info.introduction"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">保存</el-button>
      <el-button @click="onCancel()">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { deepClone } from "../../static/utils/deepClone";
import { updateInfo, getAvatarUploadUrl, refreshAvatarNew } from "../api/user";
import Avatar from "./Avatar";
import { uploadFile } from "../api/file";

export default {
  name: "SimpleInfo",
  components: { Avatar },
  mounted() {},
  created() {
    this.infoContainer = deepClone(this.$store.state.user.info);
  },
  watch: {
    icon: function(icon) {
      this.options.src = icon;
    },
    storeInfo: function() {
      this.infoContainer = deepClone(this.$store.state.user.info);
    }
  },
  computed: {
    info: function() {
      return this.infoContainer;
    },
    icon: function() {
      let icon = this.info.icon;
      if (!icon) {
        icon = "http://localhost/static/icon/DEFAULT.png";
      } else {
        icon = "http://localhost" + icon;
      }
      return icon;
    },
    select: function() {
      let locations = this.info.location.split("-");
      return {
        province: locations[0],
        city: locations[1],
        area: locations[2]
      };
    },
    storeInfo: function() {
      return this.$store.state.user.info;
    }
  },
  data() {
    return {
      size: 100,
      formLabelAlign: "left",
      infoContainer: Object,
      update: true,
      options: {
        src: this.icon,
        size: 100,
        alt: "http://localhost/static/icon/DEFAULT.png",
        accept: "image/jpeg,image/png",
        serverUrl: "",
        header: null
      },
      funcs: {
        doUpload: this.uploadAvatar,
        uploadSuccess: this.uploadSuccess,
        uploadError: this.uploadError,
        beforeUpload: this.beforeUpload
      }
    };
  },
  methods: {
    onSubmit() {
      if (!this.infoContainer.location) {
        this.$elementMessage("请完善地区信息", "error", 3000);
        return;
      }
      let info = {
        nickname: this.infoContainer.nickname,
        birthday: new Date(this.infoContainer.birthday),
        gender: this.infoContainer.gender,
        introduction: this.infoContainer.introduction,
        location: this.infoContainer.location
      };
      let _this = this;
      updateInfo(this.infoContainer.id, info)
        .then(function(res) {
          _this.$elementMessage(res.message, "success", 1500);
          _this.$store.dispatch("GetInfoByToken");
          Promise.resolve(res);
        })
        .catch(function(err) {
          _this.$elementMessage(err.message, "error", 1500);
          console.info("err:" + err);
          Promise.reject(err);
        });
    },
    onSelected(data) {
      if (data.area.code) {
        this.infoContainer.location =
          data.province.value + "-" + data.city.value + "-" + data.area.value;
      } else {
        this.infoContainer.location = "";
      }
    },
    onCancel() {
      this.infoContainer = deepClone(this.$store.state.user.info);
      this.update = false;
      let _this = this;
      setTimeout(function() {
        _this.update = true;
      }, 0);
    },
    //上传头像
    uploadAvatar(content) {
      let _this = this;
      let userId = this.$store.state.user.info.id;
      let file = content.file;
      let type = file.type.replace("image/", "");
      //获取上传到Minio的URL
      getAvatarUploadUrl(userId, type)
        .then(function(res) {
          let url = res.data;
          //直接把头像上传到Minio
          uploadFile(url, file).then(function(res) {
            //如果上传成功，则更新信息
            _this.uploadSuccess(res);
            let userId = _this.infoContainer.id;
            _this.$store.commit(
              "SET_ICON",
              "/static/icon/" + userId + "_ORIGINAL." + type
            );
          });
        })
        .catch(function(err) {
          //失败则返回报错信息
          console.info("err:" + JSON.stringify(err));
          _this.uploadError(err);
        });
    },
    uploadSuccess() {
      // this.$router.go(0);
      let _this = this;
      this.options.reload = true;
      setTimeout(function() {
        _this.options.reload = false;
      }, 50);
      // refreshAvatar(this.info.icon);
      refreshAvatarNew(this.icon).catch();
    },
    uploadError() {},
    beforeUpload() {}
  }
};
</script>

<style>
.distpicker-address-wrapper {
  width: 100%;
}
.distpicker-address-wrapper select {
  width: 32% !important;
}
.el-radio-group.info-gender {
  width: 100%;
}

.avatar-wrap {
  position: relative;
  box-sizing: border-box;
  width: 100px;
  height: 100px;
  display: inline-block;
  border-radius: 100%;
  cursor: pointer;
}

.avatar-wrap img {
  object-fit: cover;
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: 100%;
}
</style>
