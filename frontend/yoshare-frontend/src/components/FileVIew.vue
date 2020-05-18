<template>
  <div class="file-view">
    <div class="file-info" v-show="fileInfo">
      <div class="file-name">{{ fileInfo ? fileInfo.name : "" }}</div>
      <div class="info-group">
        <div class="info-group-el">
          <div class="info-title">大小</div>
          <div class="info-value">{{ size }}</div>
        </div>
        <div class="info-group-el">
          <div class="info-title">类型</div>
          <div class="info-value">{{ type }}</div>
        </div>
        <div class="info-group-el" @click="download()">
          <div class="info-title">下载</div>
          <div class="info-value">↓</div>
        </div>
      </div>
    </div>

    <video
      id="video"
      class="preview-component"
      preload="preload"
      controlsList="nodownload"
      controls="controls"
      :src="videoSrc"
      v-show="fileType === 'VIDEO'"
    ></video>
    <img
      id="image"
      class="preview-component"
      v-show="fileType === 'IMAGE'"
      crossOrigin
      :src="imageSrc"
    />
    <audio
      id="audio"
      class="preview-component"
      controls
      :src="audioSrc"
      v-show="fileType === 'AUDIO'"
    >
      您的浏览器不支持音频预览标签。
    </audio>
  </div>
</template>

<script>
export default {
  name: "FileView",
  props: {
    fileInfo: Object
  },
  computed: {
    type: function() {
      if (!this.fileInfo) return "";
      let tag = "";
      switch (this.fileType) {
        case "VIDEO": {
          tag = "视频";
          break;
        }
        case "AUDIO": {
          tag = "音频";
          break;
        }
        case "IMAGE": {
          tag = "图片";
          break;
        }
        case null: {
          tag = "文档";
          break;
        }
      }
      return this.fileInfo.type + tag;
    },
    size: function() {
      if (!this.fileInfo) return "";
      let tags = ["Byte", "KB", "MB", "GB"];
      let num = this.fileInfo.size;
      for (let index in tags) {
        if (num < 1024) {
          return num.toFixed(2) + tags[index];
        }
        num = num / 1024;
      }
      return num.toFixed(2) + "GB";
    }
  },
  data() {
    return {
      imageTag: ["png", "jpeg", "png", "gif", "jpg", "bmp"],
      videoTag: ["mp4", "mpg"],
      audioTag: ["mp3"],
      fileType: null,
      videoSrc: "",
      imageSrc: "",
      audioSrc: ""
    };
  },
  watch: {
    fileInfo: function() {
      this.pausePlaying();
      this.setType();
    }
  },
  methods: {
    setType() {
      let type = this.fileInfo.type;
      let _this = this;
      this.fileType = null;
      this.imageTag.forEach(el => {
        if (el === type) {
          _this.fileType = "IMAGE";
          _this.imageSrc = "http://127.0.0.1:9000" + _this.fileInfo.url;
          // _this.fileInfo.url = _this.fileInfo.url.replace(/^(https|http)(:\/\/)[a-zA-Z0-9.]+:[0-9]{1,}/g, "");
        }
      });
      this.videoTag.forEach(el => {
        if (el === type) {
          _this.fileType = "VIDEO";
          _this.videoSrc = "http://127.0.0.1:9000" + _this.fileInfo.url;
        }
      });
      this.audioTag.forEach(el => {
        if (el === type) {
          _this.fileType = "AUDIO";
          _this.audioSrc = "http://127.0.0.1:9000" + _this.fileInfo.url;
        }
      });
    },
    pausePlaying() {
      let video = document.getElementById("video");
      let audio = document.getElementById("audio");
      if (video) {
        video.pause();
      }
      if (audio) {
        audio.pause();
      }
    },
    download() {
      if (!this.fileInfo.url) return;

      // let url = "http://127.0.0.1:9000" + this.fileInfo.url;
      // console.info(url)
      // var form = document.createElement("form");
      // document.getElementsByTagName('body')[0].appendChild(form);
      // form.setAttribute('style','display:none');
      // form.setAttribute('target','');
      // form.setAttribute('method','get');
      // form.setAttribute('action',url);//下载文件的请求路径
      //
      // //clinicId
      // var input1 = document.createElement('input');
      // input1.setAttribute('type','hidden');
      // input1.setAttribute('name','clinicId');
      // form.appendChild(input1);
      //
      // form.submit();



      window.open("http://127.0.0.1:9000" + this.fileInfo.url);

      // let link = document.createElement("a");
      // //设置下载的文件名
      // link.download = this.fileInfo.name;
      // link.style.display = "none";
      // //设置下载路径
      // link.href = this.fileInfo.url;
      // //触发点击
      // document.body.appendChild(link);
      // link.click();
      // //移除节点
      // document.body.removeChild(link);
    }
  }
};
</script>

<style scoped>
.file-view {
  width: 100%;
  height: 100%;
}

.file-info {
  width: 100%;
  height: 65px;
  background-color: #efefef;
  box-sizing: border-box;
  overflow: hidden;
  padding: 2px;
  margin-bottom: 5px;
}

.preview-component {
  width: 100%;
}

.info-group-el {
  display: inline-block;
  width: 32%;
  background-color: #e3edf9;
  box-sizing: border-box;
  margin: 1px 2px 1px 2px;
  border-radius: 7px;
  user-select: none;
}

.info-group-el:hover {
  background-color: #cde3fa;
}

.file-name {
  font-weight: bold;
  font-size: 16px;
  overflow: hidden;
  box-sizing: border-box;
}

.info-title {
  font-size: 13px;
}

.info-value {
  font-size: 12px;
}
</style>
