<template>
  <div class="group-resource-favorite-wrap">
    <group-resource-panel
      class="resource-panel"
      :type="'FAVORITE'"
      :previewItemCallback="previewItemCallback"
      :classificationsCallback="classificationsCallBack"
      :refresh="refresh"
    ></group-resource-panel>

    <div class="note-content">
      <!--菜单栏-->
      <div class="favorite-menu" v-show="showMenu">
        <div class="web-url">{{ iframeUrl }}</div>
        <div class="jump" @click="newTab">→</div>
      </div>
      <iframe class="web-view" :src="iframeUrl"></iframe>
    </div>

    <div class="add-fav-wrap" v-show="createRes">
      <div class="add-fav">
        <div class="add-fav-del" @click.stop="createRes = false">&#215;</div>
        <label>标题</label>
        <el-input v-model="resourceName" placeholder="20字符内" maxlength="20">
        </el-input>
        <br />
        <br />
        <br />
        <div class="create-introduction">
          <label>描述</label>
          <el-input
            v-model="resourceIntroduction"
            type="textarea"
            :rows="5"
            placeholder="请输入内容，100字符内"
            maxlength="100"
          >
          </el-input>
        </div>
        <label style="margin-top: 35px">URL</label>
        <el-input
          v-model="resourceUrl"
          placeholder="网页的URL"
          maxlength="256"
        ></el-input>
        <div></div>
        <el-button type="primary" @click="addFavoriteWebPage()">创建</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import GroupResourcePanel from "@/components/group/GroupResourcePanel.vue";
import { addGroupFavPage, getGroupFavPage } from "../../api/groupResource";
export default {
  name: "GroupFavorite",
  components: { GroupResourcePanel },
  mounted() {},
  computed: {
    groupId: function() {
      return this.$route.params.groupId;
    }
  },
  data() {
    return {
      noteLoading: false,
      previewItemCallback: {
        click: this.visitFavoritePage,
        star: this.itemStar,
        unstar: this.itemUnstar,
        share: this.itemShare,
        del: null
      },
      classificationsCallBack: {
        click: this.classisClick,
        more: this.classisEdit,
        addRes: this.classisAddRes
      },
      refresh: false,
      resourceName: "",
      resourceIntroduction: "",
      resourceUrl: "",
      createRes: false,
      iframeUrl: "",
      currentClassis: null,
      destClassis: null,
      showMenu: false
    };
  },
  methods: {
    itemStar() {},
    itemUnstar() {},
    itemShare() {},
    itemDelete() {},
    classisClick() {},
    classisEdit() {},
    classisAddRes(classis) {
      this.destClassis = classis;
      this.createRes = true;
      this.addFavoriteWebPageDialog();
    },
    newTab() {
      window.open(this.iframeUrl, "_blank");
    },
    addFavoriteWebPageDialog: function() {
      document.addEventListener("click", this.clickListener);
    },
    addFavoriteWebPage() {
      this.createRes = false;
      let _this = this;
      addGroupFavPage(
        _this.groupId,
        _this.resourceName,
        this.resourceIntroduction,
        this.resourceUrl,
        _this.destClassis.id
      )
        .then(function() {
          _this.refreshData();
        })
        .catch(err => {
          console.info("err:" + err);
        });
    },
    clickListener(e) {
      let box = document.getElementsByClassName("add-fav")[0];
      if (box.contains(e.target)) return;
      this.createRes = false;
      document.removeEventListener("click", this.clickListener);
    },
    visitFavoritePage(resource) {
      this.showMenu = true;
      let _this = this;
      let webId = resource.resourceRef;
      getGroupFavPage(this.groupId, webId)
        .then(function(res) {
          _this.iframeUrl = res.data.url;
        })
        .catch(err => {
          console.info("err:" + err.message);
        });
    },
    refreshData() {
      let _this = this;
      this.refresh = true;
      setTimeout(function() {
        _this.refresh = false;
      }, 50);
    }
  }
};
</script>

<style scoped>
.group-resource-favorite-wrap {
  width: 100%;
  height: 100%;
  position: relative;
  display: flex;
  box-sizing: border-box;
  overflow: hidden;
}

.resource-panel {
  position: relative;
  width: 500px;
  height: 100%;
  box-sizing: border-box;
}

.note-content {
  width: 100px;
  height: 100%;
  flex: 1;
  box-sizing: border-box;
}

.add-fav-wrap {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1000;
  background-color: rgba(255, 255, 255, 0.8);
}

.add-fav {
  display: block;
  position: relative;
  top: 50%;
  left: 50%;
  transform: translate3d(-50%, -50%, 0);
  background-color: #e8f2fe;
  width: 300px;
  height: auto;
  border-radius: 15px;
  padding: 20px;
  box-shadow: 1px 4px 10px 2px #ccc;
  -webkit-box-shadow: 1px 4px 10px 2px #ccc;
}

.add-fav-del {
  font-size: 30px;
  line-height: 30px;
  box-sizing: border-box;
  position: absolute;
  background-color: #eee;
  width: 30px;
  height: 30px;
  top: -15px;
  right: -15px;
  border-radius: 100%;
  border: 1px solid #fff;
  cursor: pointer;
}

.favorite-menu {
  height: 40px;
  width: 100%;
  border-bottom: 1px solid #d5d5d5;
  border-top: 1px solid #d5d5d5;
  box-sizing: border-box;
  line-height: 40px;
  display: -webkit-box;
}

.web-url {
  flex: 1;
  width: 100%;
  position: relative;
  height: 34px;
  top: 2px;
  margin: 1px 10px 1px 5px;
  overflow: hidden;
  text-align: left;
  box-sizing: border-box;
  border-radius: 7px;
  padding: 0 5px 0 5px;
  background-color: #efefef;
  line-height: 34px;
  overflow-x: auto;
  font-size: 15px;
  font-family: Arial;
}

.jump {
  width: 35px;
  height: 35px;
  border-radius: 8px;
  position: relative;
  top: 2px;
  right: 5px;
  background-color: #e3effe;
  line-height: 35px;
  font-weight: bold;
  font-size: 20px;
}

.web-view {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  border: none;
  overflow: auto;
}

.web-view body {
  height: 100%;
}
</style>
