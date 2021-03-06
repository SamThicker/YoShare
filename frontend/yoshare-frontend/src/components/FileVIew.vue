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
        <div class="info-group-el" @click="downloadClicked()">
          <div class="info-title">下载</div>
          <div class="info-value">↓</div>
        </div>
      </div>
    </div>
    <div class="content-wrap">
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
      <div
        id="other"
        class="preview-component"
        v-show="fileType === 'OTHER' && finished"
      >
        抱歉，该文件不支持预览，请下载后查看。
      </div>
      <div
        id="unFinished"
        class="preview-component"
        v-show="!finished"
      >
        该文件尚未上传完毕，请继续上传
      </div>
    </div>
  </div>
</template>

<script>
// import { download } from "../api/groupFile";

export default {
  name: "FileView",
  props: {
    fileInfo: Object
  },
  computed: {
    type: function() {
      if (!this.fileInfo) return "";
      let tag = "OTHER";
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
        case "OTHER": {
          tag = "文档";
          break;
        }
      }
      return this.fileInfo.type + tag;
    },
    size: function() {
      if (!this.fileInfo || !this.fileInfo.finished) return "";
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
      videoTag: ["mp4", "mpg", "mov"],
      audioTag: ["mp3"],
      fileType: null,
      videoSrc: "",
      imageSrc: "",
      audioSrc: "",
      finished: true
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
      if (!this.fileInfo.finished) {
        this.finished = false;
        return;
      }
      this.finished = true;
      let type = this.fileInfo.type;
      let _this = this;
      this.fileType = null;
      if (this.imageTag.indexOf(type) > -1) {
        _this.fileType = "IMAGE";
        _this.imageSrc = "http://127.0.0.1:9000" + _this.fileInfo.url;
        return;
        // _this.fileInfo.url = _this.fileInfo.url.replace(/^(https|http)(:\/\/)[a-zA-Z0-9.]+:[0-9]{1,}/g, "");
      }
      if (this.videoTag.indexOf(type) > -1) {
        _this.fileType = "VIDEO";
        _this.videoSrc = "http://127.0.0.1:9000" + _this.fileInfo.url;
        return;
      }
      if (this.audioTag.indexOf(type) > -1) {
        _this.fileType = "AUDIO";
        _this.audioSrc = "http://127.0.0.1:9000" + _this.fileInfo.url;
        return;
      }
      _this.fileType = "OTHER";
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
    downloadClicked() {
      if (!this.fileInfo.url) return;
      let url = "http://127.0.0.1:9000" + this.fileInfo.url;
      window.open(url);
    },
    download(url, params) {
      let tempForm = document.createElement("form");
      tempForm.action = url;
      tempForm.method = "get";
      tempForm.style.display = "none";
      for (var x in params) {
        let opt = document.createElement("textarea");
        opt.name = x;
        opt.value = params[x];
        tempForm.appendChild(opt);
      }
      document.body.appendChild(tempForm);
      tempForm.submit();
      return tempForm;
    }
  }
};
</script>

<style scoped>
.file-view {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
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

.info-group {
  display: flex;
}

.info-group-el {
  display: inline-block;
  flex: 1;
  background-color: #e3edf9;
  box-sizing: border-box;
  margin: 1px 3px 1px 3px;
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
  white-space: nowrap;
  text-overflow: ellipsis;
}

.info-title {
  font-size: 13px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.info-value {
  font-size: 12px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.content-wrap {
  overflow: auto;
  flex: 1;
}
</style>
