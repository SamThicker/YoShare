<template>
  <div class="category-resource-wrap">
    <resource-panel class="category-resource"></resource-panel>
    <div class="resource-content"></div>
  </div>
</template>

<script>
import ResourcePanel from "@/components/ResourcePanel.vue";
export default {
  name: "CategoryResource",
  components: { ResourcePanel },
  data() {
    return {
      noteSearch: ""
    };
  }
};
</script>

<style scoped>
.category-resource-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  display: -webkit-box;
}

.category-resource {
  height: 100%;
  width: 500px;
}

.resource-content {
  background-color: #ff6464;
  height: 100%;
  flex: 1;
}
</style>
