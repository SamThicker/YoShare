<template>
  <div class="group-category-note-wrap">

    <group-resource-panel
      class="resource-panel"
      :type="'FILE'"
      :previewItemCallback="previewItemCallback"
      :classificationsCallback="classificationsCallBack"
      :refresh="refresh"
    ></group-resource-panel>

  </div>
</template>

<script>
import GroupResourcePanel from "@/components/group/GroupResourcePanel.vue";
export default {
  name: "GroupFile",
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
        addRes: this.classisAddRes
      },
      refresh: false
    };
  },
  methods: {
    itemClick() {},
    itemStar() {},
    itemUnstar() {},
    itemShare() {},
    itemDelete() {},
    classisClick() {},
    classisEdit() {},
    classisAddRes() {}
  }
};
</script>

<style scoped>
.group-category-note-wrap {
  width: 100%;
  height: 100%;
  position: relative;
}
</style>
