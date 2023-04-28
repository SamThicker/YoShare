<template>
  <div class="time-line">
    <div
      class="item"
      v-for="(item, index) in items"
      :key="index"
      @click="onClick(item.id, item.index, item.content)"
    >
      <div class="item-index">{{ item.index }}</div>
      <div class="item-content">{{ item.content }}</div>
    </div>
    <div
      class="item-tail"
      @click="
        endItem ? onClick(endItem.id, endItem.index, endItem.content) : null
      "
    >
      <div class="item-index">
        {{ endItem ? endItem.index : "Current" }}
      </div>
      <div class="item-content">
        {{ endItem ? endItem.content : "Editing" }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "TimeLine",
  props: {
    callBack: Function,
    items: Array,
    endItem: Object
  },
  data() {
    return {};
  },
  methods: {
    onClick(id, index, content) {
      if (this.callBack) {
        this.callBack(id, index, content);
      }
    }
  },
  mounted() {}
};
</script>

<style scoped>
.time-line {
  position: relative;
  height: 100%;
  width: 100%;
  box-sizing: border-box;
  padding-top: 15px;
}

.item,
.item-tail {
  width: calc(100% - 30px);
  height: auto;
  margin-left: 30px;
  position: relative;
  margin-bottom: 10px;
  cursor: pointer;
}

.item::before,
.item-tail::before {
  content: "";
  width: 11px;
  height: 11px;
  background-color: #91c2fc;
  position: absolute;
  left: -15px;
  border-radius: 100%;
}

.item::after {
  content: "";
  width: 3px;
  height: calc(100% + 10px);
  background-color: #91c2fc;
  position: absolute;
  top: 0;
  left: -11px;
}

.item:hover::before,
.item-tail:hover::before {
  transform: scale3d(1.2, 1.2, 1);
  background-color: #569ffb;
}

.item:hover::after {
  transform: scale3d(1.1, 1, 1);
  background-color: #569ffb;
}

.item-index {
  line-height: 12px;
  font-size: 12px;
  position: relative;
  text-align: left;
  color: #656565;
}

.item:hover .item-index,
.item-tail:hover .item-index {
  transform: scale3d(1.01, 1.01, 1);
  color: #343434;
}

.item-content {
  font-size: 14px;
  width: 100%;
  height: auto;
  position: relative;
  text-align: left;
  white-space: pre-wrap;
  word-wrap: break-word;
}
</style>
