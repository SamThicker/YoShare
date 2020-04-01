<template>
  <div class="category-panel-wrap">
    <div class="note-side-bar-folder">
      <div class="folders-label">
        <span class="add"><h3>+</h3></span>
        <span class="char"><h3>分类</h3></span>
      </div>
      <div class="folders">
        <ul>
          <li>
            <i class="el-icon-files"></i>
            <span>全部</span>
          </li>
          <li>
            <i class="el-icon-user"></i>
            <span>个人</span>
          </li>
          <li>
            <i class="el-icon-share"></i>
            <span>共享</span>
          </li>
          <li>
            <i class="el-icon-star-off"></i>
            <span>星标</span>
          </li>
          <li>
            <i class="el-icon-reading"></i>
            <span>自定义</span>
          </li>
        </ul>
      </div>
    </div>
    <div class="note-side-bar-preview">
      <!--菜单，搜索框-->
      <div class="preview-menu">
        <el-input
          placeholder="请输入内容"
          v-model="noteSearch"
          class="note-search"
        >
          <el-button slot="append" icon="el-icon-search"></el-button>
        </el-input>
      </div>
      <div class="preview-list-wrap">
        <ul class="preview-list">
          <li
            v-for="resource in resources"
            :key="resource.id"
            @click.stop="
              previewItemClickCallback
                ? previewItemClickCallback(resource)
                : null
            "
          >
            <div class="preview-list-item">
              <div class="preview-list-item-header">
                <span class="preview-item-title">{{ resource.title }}</span>
                <span class="preview-item-menu">
                  <i
                    class="el-icon-star-on preview-item-btn"
                    v-show="false"
                    style="color: yellow; font-size: 23px"
                    @click.stop="
                      itemUnstarCallback ? itemUnstarCallback(resource) : null
                    "
                  ></i>
                  <i
                    class="el-icon-star-off preview-item-btn"
                    v-show="true"
                    @click.stop="
                      itemStarCallback ? itemStarCallback(resource) : null
                    "
                  ></i>
                  <i
                    class="el-icon-share preview-item-btn"
                    @click.stop="
                      itemShareCallback ? itemShareCallback(resource) : null
                    "
                  ></i>
                  <i
                    class="el-icon-delete preview-item-btn"
                    @click.stop="itemDelCallback ? itemDelCallback(resource) : null"
                  ></i>
                </span>
              </div>
              <div class="preview-list-item-description">
                {{ resource.description }}
              </div>
              <div class="preview-list-item-tag">
                <span class="item-date">{{ resource.datetime }}</span>
                <span class="item-author">{{ "by:" + resource.byUserId }}</span>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ResourcePanel",
  props: {
    previewItemClickCallback: Function,
    resources: Array,
    itemStarCallback: Function,
    itemUnstarCallback: Function,
    itemShareCallback: Function,
    itemDelCallback: Function
  },
  data() {
    return {
      noteSearch: "",
      defaultResourceData: [
        {
          id: "1",
          type: "NOTE",
          classification: "",
          byUserId: 24,
          description: "测试",
          title: "test",
          datetime: "2020-03-28 16:34:44",
          resourceRef: "5e7f7ca3d7b5f522aa998b60"
        }
      ]
    };
  },
  computed: {},
  watch: {
    resourceData: function() {},
    deep: true
  },
  methods: {}
};
</script>

<style scoped>
.category-panel-wrap {
  position: relative;
  height: 100%;
  width: 100%;
}

.note-side-bar-folder {
  position: relative;
  float: left;
  display: inline-block;
  height: 100%;
  width: 220px;
  background-color: #f2f2f2;
  overflow-y: scroll;
}

.note-side-bar-preview {
  position: relative;
  float: left;
  display: inline-block;
  height: 100%;
  width: 280px;
  box-sizing: border-box;
  border: 1px solid #ddd;
  overflow-y: scroll;
}

ul,
li {
  padding: 0;
  margin: 0;
  list-style: none;
}

.folders {
  height: calc(100% - 65px);
  width: 100%;
  box-sizing: border-box;
  overflow-y: hidden;
}

.folders-label {
  cursor: pointer;
  box-sizing: border-box;
}

.folders-label span {
  display: inline-block;
  box-sizing: border-box;
  height: 100%;
}

.folders-label .add {
  box-sizing: border-box;
  line-height: 80%;
  font-size: 20px;
  margin-right: 5px;
  display: none;
}

.folders-label:hover .add {
  display: inline-block;
}

.folders ul {
  height: 100%;
  width: 100%;
  box-sizing: border-box;
}

.folders ul li {
  position: relative;
  width: 100%;
  height: 38px;
  line-height: 38px;
  box-sizing: border-box;
  padding: 0 2px 0 35px;
  background-color: #f2f2f2;
  cursor: pointer;
  border-radius: 5px;
  text-align: left;
}

.folders ul li::before {
  content: "";
  position: absolute;
  left: 50%;
  transform: translate3d(-50%, 0, 0);
  width: 100%;
  height: 1px;
  background-image: linear-gradient(
    to right,
    #e8f2fe,
    #d1e7fe,
    #d1e7fe,
    #e8f2fe
  );
}

.folders ul li:hover {
  background-color: #e8f2fe;
}

.folders ul li i {
  line-height: 38px;
  font-size: 18px;
  font-weight: bold;
}

.folders ul li span {
  margin-left: 25px;
  line-height: 38px;
  font-size: 15px;
}

.preview-menu {
  height: 65px;
  width: 100%;
  box-sizing: border-box;
  border-bottom: 1px solid #ddd;
}

.note-search {
  margin: 11px auto;
  border-radius: 50%;
  width: 90%;
}

.preview-list-wrap {
  width: 100%;
  height: calc(100% - 65px);
  overflow-y: hidden;
}

.preview-list {
  height: 100%;
  width: 100%;
  overflow-y: scroll;
  padding: 5px;
  box-sizing: border-box;
}

.preview-list-item {
  position: relative;
  padding: 5px 10px;
  box-sizing: border-box;
  margin: 5px;
  background-color: #e8f2fe;
  border-radius: 5px;
  height: 125px;
  text-align: left;
  overflow: hidden;
}

.preview-item-title {
  width: calc(100% - 60px);
  font-size: 13px;
  color: #2b2b2b;
}

.preview-item-menu {
  position: absolute;
  right: 5px;
  top: 5px;
  z-index: 100;
  width: 86px;
  display: none;
  font-size: 18px;
  height: 22px;
  padding-left: 10px;
  box-sizing: border-box;
  background-color: #e8f2fe;
}

.preview-list-item:hover .preview-item-menu {
  display: inline-block;
}

.preview-item-menu i {
  margin: 0 2px;
  cursor: pointer;
}

.preview-item-menu i:hover {
  color: #68aafb;
}

.preview-list-item-description {
  width: 100%;
  height: calc(100% - 54px);
  font-size: 14px;
  box-sizing: border-box;
  color: #888890;
  white-space: pre-wrap;
  word-wrap: break-word;
  overflow: hidden;
  margin-top: 2px;
}

.preview-list-item-tag {
  font-size: 10px;
  color: #cacaca;
}

.item-author {
  float: right;
}
</style>
