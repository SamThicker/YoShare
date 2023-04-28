<template>
  <div class="category-note-wrap" v-loading="deleteLoading">
    <resource-panel
      class="resource-panel"
      :type="'NOTE'"
      :previewItemCallback="previewItemCallback"
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
      previewItemCallback: {
        click: this.noteItemClicked,
        star: this.itemStarClicked,
        unstar: this.itemUnstarClicked,
        share: this.itemShareClicked,
        del: null
      },
      classificationsCallBack: {
        click: null,
        addRes: this.addNote
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
    itemShareClicked: function() {},
    addNote: function(classis) {
      let classId = classis.id;
      let path = "/workBench/" + this.userId;
      let query = {};
      if (classId > 0) {
        query.classId = classId;
      }
      this.$router.push({ path: path, query: query });
    }
  }
};
</script>

<style scoped>
.category-note-wrap {
  position: relative;
  height: 100%;
  width: 100%;
  /*display: -webkit-box;*/
  display: flex;
}

.resource-panel {
  width: 500px;
  height: 100%;
}

.note-content {
  width: 100%;
  /*height: 100%;*/
  /*flex: 1;*/
  /*position: relative;*/
  display: inline-block;
  position: relative;
  height: 100%;
  width: 10px;
  /*width: 100%;*/
  /*flex: 1;*/
  flex-grow: 1;
  flex-shrink: 1;
  border: 1px solid #eee;
  box-sizing: border-box;
}
</style>
