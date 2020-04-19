<template>
  <div class="file-content-wrap">
    <div class="image-wrap">

    </div>
  </div>
</template>

<script>
export default {
  name: "FileContent",
  props: {
    file: Object
  },
  imageType: ["jpeg", "png", ""],
  methods: {

  }
};
</script>

<style scoped>
.file-content-wrap {
  width: 100%;
  height: 100%;
  background-color: #ff6464;
}
</style>
