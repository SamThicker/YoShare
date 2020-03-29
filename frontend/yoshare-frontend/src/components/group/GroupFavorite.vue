<template>
  <div class="group-resource-favorite-wrap">
    <resource-panel
      :preview-item-click-callback="previewItemClickCallback"
    ></resource-panel>
  </div>
</template>

<script>
import ResourcePanel from "@/components/ResourcePanel";
export default {
  name: "Favorite",
  components: { ResourcePanel },
  mounted() {
    this.previewItemClickCallback = this.previewItemClicked;
  },
  data() {
    return {
      previewItemClickCallback: Function
    };
  },
  methods: {
    previewItemClicked: function(index) {
      console.info("index:" + index);
    }
  }
};
</script>

<style scoped>
.group-resource-favorite-wrap {
  width: 100%;
  height: 100%;
  position: relative;
}
</style>
