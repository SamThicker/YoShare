<template>
  <div class="category-note-wrap" v-loading="deleteLoading">
    <resource-panel
      class="resource-panel"
      :type="'NOTE'"
      :previewItemClickCallback="noteItemClicked"
      :itemStarCallback="itemStarClicked"
      :itemUnstarCallback="itemUnstarClicked"
      :itemShareCallback="itemShareClicked"
      :classificationsCallback="classificationsCallBack"
      :refresh="refresh"
    ></resource-panel>
    <div class="note-content" v-loading="noteLoading">
      <router-view />
    </div>
  </div>
</template>

<script>
import ResourcePanel from "@/components/ResourcePanel.vue";
export default {
  name: "MemberResourceNote",
  components: { ResourcePanel },
  mounted() {},
  data() {
    return {
      noteSearch: "",
      deleteLoading: false,
      noteLoading: false,
      note: null,
      toTop: false,
      classificationsCallBack: {
        click: null,
        addRes: null
      },
      refresh: false
    };
  },
  computed: {
    userId: function() {
      return this.$store.state.user.info.id;
    }
  },
  watch: {
    userId: function() {},
    note: function() {}
  },
  methods: {
    //资源预览项点击事件
    noteItemClicked: function(resource) {
      this.toTop = true;
      let redirection = "/resource/note/" + resource.resourceRef;
      if (redirection === this.$route.path) return;
      this.$router.push(redirection);
    },
    itemStarClicked: function() {},
    itemUnstarClicked: function() {},
    itemShareClicked: function() {}
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

.resource-panel {
  width: 500px;
  height: 100%;
}

.note-content {
  height: 100%;
  flex: 1;
  border: 1px solid #eee;
  box-sizing: border-box;
}
</style>
