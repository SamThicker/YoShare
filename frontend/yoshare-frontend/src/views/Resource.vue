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
      src="http://localhost:9000/resource/ins_TXBbCwcvefYqZWXOjGTG48AeA62asVUz.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=admin%2F20200309%2F%2Fs3%2Faws4_request&X-Amz-Date=20200309T162625Z&X-Amz-Expires=432000&X-Amz-SignedHeaders=host&X-Amz-Signature=0626c9f30f350411207306d52ad1639ac231e316e0ee2303e3dfb5c8ebc409f0"
    ></video>
  </div>
</template>

<script>
import { getUploadUrl, uploadFile } from "../api/resource";
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
