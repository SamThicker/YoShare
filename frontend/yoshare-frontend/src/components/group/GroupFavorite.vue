<template>
  <div class="group-resource-favorite-wrap">

    <group-resource-panel
      class="resource-panel"
      :type="'FAVORITE'"
      :previewItemCallback="previewItemCallback"
      :classificationsCallback="classificationsCallBack"
      :refresh="refresh"
    ></group-resource-panel>

  </div>
</template>

<script>
import GroupResourcePanel from "@/components/group/GroupResourcePanel.vue";
export default {
  name: "GroupFavorite",
  components: { GroupResourcePanel },
  mounted() {
  },
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
.group-resource-favorite-wrap {
  width: 100%;
  height: 100%;
  position: relative;
}
</style>
