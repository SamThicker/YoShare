<template>
  <div class="avatar-wrap myavatar">
    <!-- 图片上传组件辅助 -->
    <el-upload
      :accept="options ? options.accept : 'image/png'"
      class="avator-upload"
      :action="options ? options.serverUrl : null"
      :http-request="funcs ? funcs.doUpload : null"
      name="img"
      :headers="options ? options.header : null"
      :show-file-list="false"
      :on-success="funcs ? funcs.uploadSuccess : null"
      :on-error="funcs ? funcs.uploadError : null"
      :before-upload="funcs ? funcs.beforeUpload : null"
      style="display: none"
    >
    </el-upload>
    <img
      :src="options ? options.src : null"
      :alt="options ? options.alt : ''"
      @click="selectAvatar()"
    />
  </div>
</template>

<script>
export default {
  inject: ["reload"],
  name: "Avatar",
  props: {
    options: {
      src: String,
      size: Number,
      alt: String,
      accept: String,
      serverUrl: String,
      header: Object,
      reload: Boolean
    },
    funcs: {
      doUpload: Function,
      uploadSuccess: Function,
      uploadError: Function,
      beforeUpload: Function
    }
  },
  data() {
    return {};
  },
  mounted() {
    this.setSize(this.options.size);
  },
  watch: {
    isReload: function(bool) {
      if (bool) this.reload();
    },
    size: function(val) {
      this.setSize(val);
    },
    avatarReload: function(bool) {
      if (bool) {
        this.reload();
      }
    },
    icon: function() {
      console.info("icon changed");
      console.info(this.options.src)
      // this.options.src = this.options.src;
    },
    deep: true
  },
  computed: {
    isReload: function() {
      return this.options.reload;
    },
    avatarReload: function() {
      return this.options.reload;
    },
    // icon: function() {
    //   // return this.options.src;
    // }
  },
  methods: {
    selectAvatar() {
      document.querySelector(".el-upload__input").click();
    },
    setSize(size) {
      let el = document.querySelector(".avatar-wrap.myavatar");
      if (this.options.size) {
        el.style.height = el.style.width = size + "px";
      } else {
        el.style.height = el.style.width = "40px";
      }
    }
  }
};
</script>

<style scoped>
.avatar-wrap {
  display: inline-block;
  position: relative;
  box-sizing: border-box;
  border-radius: 100%;
  cursor: pointer;
  margin: auto;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
}

.avatar-wrap img {
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: 100%;
  object-fit: cover;
}
</style>
