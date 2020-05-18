<template>
<!--  <div class="resource-panel-wrap">-->
  <div>
    <div class="note-side-bar-folder">
      <div class="folders-label" @click.stop="addClassDialog()">
        <span class="add"><h3>+</h3></span>
        <span class="char"><h3>分类</h3></span>
      </div>
      <div class="folders">
        <ul>
          <transition-group name="el-fade-in">
            <li
              class="classification"
              v-for="classification in classifications"
              :key="classification.id"
              :class="{
                unEditable: classification.unEditable,
                unNameable: classification.unNameable,
                active: currentClassification.id
                  ? classification.id === currentClassification.id
                  : false
              }"
              @click.stop="selectClassis(classification)"
            >
              <i
                :class="
                  classification.icon ? classification.icon : 'el-icon-reading'
                "
              ></i>
              <span class="classification-name">{{
                classification ? classification.classificationName : null
              }}</span>
              <span class="classification-menu">
                <span
                  class="classification-add"
                  @click.stop="classificationsCallback.addRes(classification)"
                  >+</span
                >
                <span
                  class="classification-more"
                  @click.stop="moreDialog(classification)"
                  >...</span
                >
              </span>
            </li>
          </transition-group>
        </ul>
      </div>
    </div>
    <div class="note-side-bar-preview">
      <!--菜单，搜索框-->
<!--      <div class="preview-menu">-->
<!--        <el-input-->
<!--          placeholder="请输入内容"-->
<!--          v-model="noteSearch"-->
<!--          class="note-search"-->
<!--        >-->
<!--          <el-button slot="append" icon="el-icon-search"></el-button>-->
<!--        </el-input>-->
<!--      </div>-->
      <div class="preview-list-wrap">
        <ul class="preview-list">
          <transition-group name="el-fade-in">
            <li
              v-for="resource in resources"
              :key="resource.id"
              @click.stop="previewItemCallback.click ?
              previewItemCallback.click(resource) : null
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
                        previewItemCallback.unstar
                          ? previewItemCallback.unstar(resource)
                          : null
                      "
                    ></i>
                    <i
                      class="el-icon-star-off preview-item-btn"
                      v-show="true"
                      @click.stop="
                        previewItemCallback.star
                          ? previewItemCallback.star(resource)
                          : null
                      "
                    ></i>
                    <i
                      class="el-icon-share preview-item-btn"
                      @click.stop="
                        previewItemCallback.share
                          ? previewItemCallback.share(resource)
                          : null
                      "
                    ></i>
                    <i
                      class="el-icon-delete preview-item-btn"
                      @click.stop="
                        previewItemCallback.del
                          ? previewItemCallback.del(resource)
                          : itemDelClicked(resource)
                      "
                    ></i>
                  </span>
                </div>
                <div class="preview-list-item-description">
                  {{ resource.description }}
                </div>
                <div class="preview-list-item-tag">
                  <span class="item-date">{{ resource.datetime }}</span>
                  <span class="item-author">{{
                    "by:" + resource.byUserId
                  }}</span>
                </div>
              </div>
            </li>
          </transition-group>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import {
  addMemClassification,
  deleteMemClassification,
  delResourceNote,
  getMemClassification,
  getOwnResource,
  updateMemClassification
} from "@/api/resource";
import { formatDateTime } from "../../static/utils/dateUtil";

export default {
  name: "ResourcePanel",
  props: {
    type: String,
    previewItemCallback: {
      click: Function,
      star: Function,
      unstar: Function,
      del: Function,
      share: Function
    },
    classificationsCallback: {
      click: Function,
      more: Function,
      addRes: Function
    },
    refresh: Boolean
  },
  mounted() {
    this.init();
    this.getOwnResource();
    this.getClassifications();
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
      ],
      resources: [],
      classifications: [],
      ownClassifications: [],
      basicClassifications: [
        {
          id: "-1",
          icon: "el-icon-files",
          classificationName: "全部",
          // unEditable: true,
          unNameable: true
        },
        {
          id: "-2",
          icon: "el-icon-user",
          classificationName: "个人",
          unEditable: true
        },
        {
          id: "-3",
          icon: "el-icon-share",
          classificationName: "共享",
          unEditable: true
        },
        {
          id: "-4",
          icon: "el-icon-star-off",
          classificationName: "星标",
          unEditable: true
        }
      ],
      currentClassification: ""
    };
  },
  computed: {
    userId: function() {
      return this.$store.state.user.info.id;
    }
  },
  watch: {
    refresh: function(bool) {
      if (!bool) return;
      this.getOwnResource();
      this.getClassifications();
    },
    resourceData: function() {},
    ownClassifications: function(val) {
      this.classifications.length = 0;
      this.classifications.push(...this.basicClassifications);
      this.classifications.push(...val);
    },
    userId: function(id) {
      if (id) {
        this.getOwnResource();
        this.getClassifications();
      }
    },
    currentClassification: function() {
      this.getOwnResource();
    },
    deep: true
  },
  methods: {
    init: function() {
      this.classifications.push(...this.basicClassifications);
      this.currentClassification = this.classifications[0];
    },
    getOwnResource: function() {
      let _this = this;
      if (!this.userId) return;
      let type = this.type ? this.type : "all";
      getOwnResource(_this.userId, type, this.currentClassification.id)
        .then(function(res) {
          _this.resources = res.data;
          _this.resources.forEach(res => {
            res.datetime = formatDateTime(res.datetime);
          });
        })
        .catch(function(err) {
          console.info("err:" + err);
        });
    },
    itemDelClicked(resource) {
      let _this = this;
      this.$confirm("此操作将永久删除该资源, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          _this.deleteLoading = true;
          resource.datetime = new Date(resource.datetime);
          delResourceNote(_this.$store.state.user.info.id, resource)
            .then(function() {
              let res = resource;
              let index = _this.resources.findIndex(
                resource => resource.id === res.id
              );
              if (index > -1) {
                _this.resources.splice(index, 1);
              }
              _this.deleteLoading = false;
            })
            .catch(() => {
              _this.$elementMessage("操作失败，请重试", "error", 1000);
              _this.deleteLoading = false;
            });
        })
        .catch(() => {
          _this.deleteLoading = false;
        });
    },
    getClassifications() {
      let _this = this;
      let type = this.type ? this.type : "all";
      getMemClassification(this.userId, type)
        .then(function(res) {
          _this.ownClassifications = res.data;
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
          if (!value || "" === value) {
            _this.$elementMessage("输入不能为空", "error", 1000);
            return;
          }
          let name = value;
          let userId = _this.userId;
          let type = _this.type ? _this.type : "NOTE";
          addMemClassification(userId, type, name)
            .then(function() {
              _this.getClassifications();
              _this.$elementMessage("操作成功", "success", 1000);
            })
            .catch(err => {
              _this.$elementMessage("出错辣，" + err.message, "error", 1000);
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消输入"
          });
        });
    },
    selectClassis(classification) {
      this.currentClassification = classification;
      if (this.classificationsCallback && this.classificationsCallback.click) {
        this.classificationsCallback.click(classification);
      }
    },
    //显示"分类"编辑框
    moreDialog(classis) {
      if (!classis) return;
      let _this = this;
      this.$prompt("请输入新名称", "提示", {
        confirmButtonText: "重命名",
        cancelButtonText: "删除",
        inputValue: classis.classificationName,
        distinguishCancelAndClose: true,
        // inputPattern: /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
        inputErrorMessage: "格式不正确"
      })
        .then(({ value }) => {
          classis.classificationName = value;
          _this.updateClassis(classis);
        })
        .catch(action => {
          if (action === "close") return;
          _this
            .$confirm("此操作将永久删除该分类, 是否继续?", "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            })
            .then(function() {
              _this.deleteClassis(classis);
            })
            .catch(() => {
              _this.$message({
                type: "info",
                message: "已取消"
              });
            });
        });
    },
    //删除"分类"
    deleteClassis(classis) {
      let _this = this;
      deleteMemClassification(this.userId, classis.id)
        .then(function() {
          _this.getClassifications();
          _this.$elementMessage("操作成功", "success", 1000);
        })
        .catch(err => {
          _this.$elementMessage(err.message, "error", 1000);
        });
    },
    //更新分类的名称
    updateClassis(classis) {
      let _this = this;
      updateMemClassification(_this.userId, classis)
        .then(function() {
          _this.$elementMessage("操作成功", "success", 1000);
          _this.getClassifications();
        })
        .catch(err => {
          _this.$elementMessage(
            "操作失败，请重试(" + err.message + ")",
            "error",
            1000
          );
        });
    }
  }
};
</script>

<style scoped>
/*.resource-panel-wrap {*/
/*  position: relative;*/
/*  height: 100%;*/
/*  width: 100%;*/
/*}*/

/*分类*/
.note-side-bar-folder {
  position: relative;
  float: left;
  display: inline-block;
  height: 100%;
  width: 220px;
  background-color: #f2f2f2;
  overflow-y: scroll;
  box-sizing: border-box;
}

/*资源列表*/
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
  overflow-y: auto;
}

.folders-label {
  cursor: pointer;
  box-sizing: border-box;
  height: 65px;
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
  border-radius: 5px;
  text-align: left;
  overflow: hidden;
}

.classification::before {
  content: "";
  position: absolute;
  left: 50%;
  transform: translate3d(-50%, 0, 0);
  width: 80%;
  height: 1px;
  background-image: linear-gradient(
    to right,
    #e8f2fe,
    #d1e7fe,
    #d1e7fe,
    #e8f2fe
  );
}

.classification:hover:not(.active) {
  background-color: #e8f2fe;
}

.classification.active {
  background-color: #d1e7fe;
}

.classification.active::before {
  display: none;
}

.classification:hover:not(.unEditable) .classification-menu {
  display: inline-block;
}

.folders ul li i {
  line-height: 36px;
  font-size: 18px;
  font-weight: bold;
  position: relative;
  display: inline-block;
  bottom: 10px;
}

.folders ul li span {
  margin-left: 25px;
  line-height: 35px;
  height: 35px;
  font-size: 15px;
  overflow: hidden;
}

.classification-menu {
  position: absolute;
  z-index: 100;
  top: 1px;
  right: 10px;
  width: 56px;
  height: 35px !important;
  display: none;
  background-color: #e8f2fe;
  overflow: hidden;
}

.classification.active .classification-menu {
  background-color: #d1e7fe;
}

.unNameable .classification-more {
  display: none;
}

.classification-name {
  max-width: 100px;
  width: auto;
  display: inline-block;
  top: 3px;
  position: relative;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.classification-add {
  position: absolute;
  font-size: 23px !important;
  font-weight: bold;
  right: 30px;
  width: 20px;
  cursor: pointer;
  user-select: none;
}

.classification-more {
  position: absolute;
  width: 20px;
  top: -3px;
  right: 5px;
  font-size: 18px !important;
  font-weight: bold;
  cursor: pointer;
  user-select: none;
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
  height: 100%;
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
