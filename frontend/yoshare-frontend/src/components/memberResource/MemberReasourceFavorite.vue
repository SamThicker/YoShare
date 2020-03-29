<template>
  <div class="category-favorite-wrap">
    <resource-panel
      class="category-panel"
      :preview-item-click-callback="previewItemClickCallback"
    ></resource-panel>
    <div class="note-content"></div>
  </div>
</template>

<script>
import ResourcePanel from "@/components/ResourcePanel.vue";
export default {
  name: "MemberResourceFavorite",
  components: { ResourcePanel },
  data() {
    return {
      previewItemClickCallback: Function
    };
  },
  mounted() {
    this.previewItemClickCallback = this.itemClick;
  },
  methods: {
    itemClick: function(index) {
      console.info("index:" + index);
    }
  }
};
</script>

<style scoped>
.category-favorite-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  display: -webkit-box;
}

.category-panel {
  position: relative;
  width: 500px;
  height: 100%;
}

.note-content {
  width: 100px;
  height: 100%;
  background-color: #ff6464;
  flex: 1;
}
</style>
