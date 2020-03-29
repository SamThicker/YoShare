<template>
  <div class="category-note-wrap">
    <resource-panel
      class="resource-note"
      :resources="resources"
    ></resource-panel>
    <div class="note-content"></div>
  </div>
</template>

<script>
import ResourcePanel from "@/components/ResourcePanel.vue";
import { getOwnResource } from "@/api/resource";
export default {
  name: "MemberResourceNote",
  components: { ResourcePanel },
  mounted() {
    let _this = this;
    setTimeout(function() {
      getOwnResource(_this.$store.state.user.info.id)
        .then(function(res) {
          _this.resources = [];
          _this.resources.push(...res.data);
        })
        .catch(function(err) {
          console.info("err:" + err);
        });
    }, 0);
  },
  data() {
    return {
      noteSearch: "",
      resources: new Array()
    };
  }
};
</script>

<style scoped>
.category-note-wrap {
  position: relative;
  height: 100%;
  width: 100%;
  display: -webkit-box;
}

.resource-note {
  width: 500px;
  height: 100%;
}

.note-content {
  background-color: #ff6464;
  height: 100%;
  flex: 1;
}
</style>
