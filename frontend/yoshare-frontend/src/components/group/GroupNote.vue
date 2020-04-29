<template>
  <div class="group-resource-note-wrap">

    <group-resource-panel
      class="resource-panel"
      :type="'NOTE'"
      :previewItemCallback="previewItemCallback"
      :classificationsCallback="classificationsCallBack"
      :refresh="refresh"
    ></group-resource-panel>

    <div class="note-content" v-loading="noteLoading">
      <router-view/>
    </div>
  </div>
</template>

<script>
import GroupResourcePanel from "./GroupResourcePanel";
export default {
  name: "GroupNote",
  components: { GroupResourcePanel },
  data() {
    return {
      noteLoading: false,
      previewItemCallback: {
        click: this.itemClick,
        star: this.itemStar,
        unstar: this.itemUnstar,
        share: this.itemShare,
        del: this.itemDelete
      },
      classificationsCallBack: {
        click: this.classisClick,
        more: this.classisEdit,
        addRes: this.addNote
      },
      refresh: false
    };
  },
  computed: {
    userId: function() {
      return this.$store.state.user.info.id;
    },
    groupId: function() {
      return this.$route.params.groupId;
    }
  },
  methods: {
    itemClick() {},
    itemStar() {},
    itemUnstar() {},
    itemShare() {},
    itemDelete() {},
    classisClick() {},
    classisEdit() {},
    addNote: function(classis) {
      let classId = classis.id;
      let path = "/workBench/" + this.userId;
      let query = {};
      if (classId > 0) {
        query.classId = classId;
      }
      query.groupId = this.groupId;
      this.$router.push({ path: path, query: query });
    }
  }
};
</script>

<style scoped>
.group-resource-note-wrap {
  width: 100%;
  height: 100%;
  position: relative;
  display: -webkit-box;
  -webkit-box-orient: horizontal;
}

.resource-panel {
  width: 500px;
  height: 100%;
}

.note-content {
  height: 100%;
  flex: 1;
  width: 100%;
  border: 1px solid #eee;
  box-sizing: border-box;
}
</style>
