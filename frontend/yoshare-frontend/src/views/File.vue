<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <header-bar-Pro :switch_able="false">
      <template v-slot:major>
        <header-menu></header-menu>
      </template>
    </header-bar-Pro>
    <input type="file" id="file" />
    <el-button @click="a()">123</el-button>
    <video
      preload="preload"
      controlsList="nodownload"
      controls="controls"
      src="http://localhost:9000/resource/ins_TXBbCwcvefYqZWXOjGTG48AeA62asVUz.mp4"
    ></video>
  </div>
</template>

<script>
import { getUploadUrl, uploadFile } from "../api/file";
import HeaderMenu from "../components/HeaderMenu";
import HeaderBarPro from "../components/HeaderBarPro";

export default {
  name: "Resource",
  components: { HeaderBarPro, HeaderMenu },
  methods: {
    a() {
      //获取到选中的文件
      let file = document.querySelector("#file").files[0];
      let url = "";
      getUploadUrl(file.name)
        .then(function(res) {
          url = res.data;
          let headers = {
            "Content-Type": file.type
          };
          uploadFile(url, file, headers)
            .then(function(res) {
              console.info(res);
            })
            .catch(function(err) {
              console.info(err);
            });
          console.info(res);
        })
        .catch(function(err) {
          console.info(err.message);
          return Promise.reject(err);
        });
    }
  }
};
</script>

<style scoped></style>
