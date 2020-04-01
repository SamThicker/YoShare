<template>
  <div class="category-note-wrap" v-loading="deleteLoading">
    <resource-panel
      class="resource-note"
      :resources="resources"
      :previewItemClickCallback="noteItemClicked"
      :itemStarCallback="itemStarClicked"
      :itemUnstarCallback="itemUnstarClicked"
      :itemShareCallback="itemShareClicked"
      :itemDelCallback="itemDelClicked"
    ></resource-panel>
    <div class="note-content" v-loading="noteLoading">
      <router-view/>
    </div>
  </div>
</template>

<script>
import ResourcePanel from "@/components/ResourcePanel.vue";
import { getOwnResource, delResourceNote } from "@/api/resource";
export default {
  name: "MemberResourceNote",
  components: { ResourcePanel },
  mounted() {
    this.getOwnResource();
  },
  data() {
    return {
      noteSearch: "",
      resources: [],
      deleteLoading: false,
      noteLoading: false,
      note: null,
      toTop: false
    };
  },
  computed: {
    userId: function() {
      return this.$store.state.user.info.id;
    }
  },
  watch: {
    userId: function(id) {
      if (id) {
        this.getOwnResource();
      }
    },
    note: function() {}
  },
  methods: {
    getOwnResource: function() {
      let _this = this;
      if (!this.userId) return;
      getOwnResource(_this.userId)
        .then(function(res) {
          // _this.resources = [];
          _this.resources = res.data;
        })
        .catch(function(err) {
          console.info("err:" + err);
        });
    },
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
    itemDelClicked(resource) {
      let _this = this;
      this.$confirm("此操作将永久删除该笔记, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          _this.deleteLoading = true;
          delResourceNote(_this.$store.state.user.info.id, resource)
            .then(function() {
              let index = _this.resources.indexOf(resource);
              if (index > -1) {
                _this.resources.splice(index, 1);
              }
              _this.deleteLoading = false;
            })
            .catch(() => {
              _this.$elementMessage("操作失败，请重试", "error", 1500);
              _this.deleteLoading = false;
            });
        })
        .catch(() => {
          _this.deleteLoading = false;
        });
    }
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
  height: 100%;
  flex: 1;
  border: 1px solid #eee;
  box-sizing: border-box;
}
</style>
