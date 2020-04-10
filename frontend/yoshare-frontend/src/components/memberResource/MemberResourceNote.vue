<template>
  <div class="category-note-wrap" v-loading="deleteLoading">
    <resource-panel
      class="resource-panel"
      :resources="resources"
      :previewItemClickCallback="noteItemClicked"
      :itemStarCallback="itemStarClicked"
      :itemUnstarCallback="itemUnstarClicked"
      :itemShareCallback="itemShareClicked"
      :itemDelCallback="itemDelClicked"
      :ownClassifications="classifications"
      :classificationsCallback="classificationsCallBack"
    ></resource-panel>
    <div class="note-content" v-loading="noteLoading">
      <router-view />
    </div>
  </div>
</template>

<script>
import ResourcePanel from "@/components/ResourcePanel.vue";
import { getOwnResource, delResourceNote } from "@/api/resource";
import { formatDateTime } from "../../../static/utils/dateUtil";
import { addMemClassification, getMemClassification } from "../../api/resource";
export default {
  name: "MemberResourceNote",
  components: { ResourcePanel },
  mounted() {
    this.getOwnResource();
    this.getNoteClassifications();
  },
  data() {
    return {
      noteSearch: "",
      resources: [],
      deleteLoading: false,
      noteLoading: false,
      note: null,
      toTop: false,
      classifications: [],
      classificationsCallBack: {
        click: null,
        add: this.addClassDialog,
        more: null,
        addRes: null
      }
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
        this.getNoteClassifications();
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
          _this.resources.forEach(res => {
            res.datetime = formatDateTime(res.datetime);
          });
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
    },
    getNoteClassifications() {
      let _this = this;
      getMemClassification(this.userId, "NOTE")
        .then(function(res) {
          let classes = res.data;
          _this.classifications = classes;
        })
        .catch(err => {
          console.info("出错辣，请稍后再试" + err);
        });
    },
    addClassDialog() {
      let _this = this;
      this.$prompt("请输入名称", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        // inputPattern: /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
        inputErrorMessage: "格式不正确"
      })
        .then(({ value }) => {
          let name = value;
          let userId = _this.userId;
          addMemClassification(userId, "NOTE", name)
            .then(function() {
              _this.getNoteClassifications();
              _this.$elementMessage("操作成功", "success", 1500);
            })
            .catch(err => {
              console.info("出错辣" + err);
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消输入"
          });
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
