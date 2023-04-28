<template>
  <div v-loading="loading">
    <div class="group-wrap">
      <transition-group name="el-fade-in">
        <div
          class="group"
          v-for="group in allGroups"
          :key="group.id"
          @click="toGroupManager(group.id)"
        >
          <div class="admin" v-show="group.adminId === userId">
            <i class="el-icon-cpu"></i>
          </div>
          <div class="avatar">
            <img :src="'http://localhost' + group.icon" />
          </div>
          <div class="group-name">{{ group.name }}</div>
          <div class="group-data">
            <div class="data-cell">
              <label>ID</label>
              <p>{{ group.id }}</p>
            </div>
            <div class="data-cell">
              <label>成员数</label>
              <p>{{ group.memberNum }}</p>
            </div>
          </div>
          <div class="group-introduction">
            <p>
              {{ group.introduction }}
            </p>
          </div>
        </div>
      </transition-group>

      <!--创建小组-->
      <div
        class="group"
        @click.stop="showCreateBar"
        v-show="ownGroups.length < 3"
      >
        <div class="add">+</div>
      </div>

      <!--加入小组-->
      <div class="group" @click="joinGroupDialog()">
        <div class="add">
          <i class="el-icon-s-custom"></i>
        </div>
      </div>
    </div>

    <!-- 添加新的小组 -->
    <div class="group-create" v-show="createBar">
      <div class="group-create-bar">
        <div class="group-create-bar-delete" @click="createBar = false">
          &#215;
        </div>
        <label>小组名</label>
        <el-input v-model="newGroup.name" placeholder="20字符内" maxlength="20">
        </el-input>
        <br />
        <br />
        <br />
        <div class="create-introduction">
          <label>简介</label>
          <el-input
            v-model="newGroup.introduction"
            type="textarea"
            :rows="5"
            placeholder="请输入内容，100字符内"
            maxlength="100"
          >
          </el-input>
        </div>
        <el-button type="primary" @click="createGroup">创建</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { createGroup, joinGroupByCode } from "../api/group";

export default {
  name: "Group",
  data() {
    return {
      ownGroups: [],
      allGroups: [],
      createBar: false,
      newGroup: {
        name: "",
        introduction: ""
      },
      loading: false
    };
  },
  watch: {
    deep: true,
    userOwnGroups: function(val) {
      this.ownGroups = val;
    },
    userAllGroups: function(val) {
      this.allGroups = val;
    },
    userId: function() {}
  },
  mounted() {
    this.ownGroups = this.userOwnGroups;
    this.allGroups = this.userAllGroups;
  },
  computed: {
    userId: function() {
      return this.$store.state.user.info.id;
    },
    userOwnGroups: function() {
      return this.$store.state.user.ownGroups;
    },
    userAllGroups: function() {
      return this.$store.state.user.allGroups;
    }
  },
  methods: {
    refreshGroups() {
      let userId = this.$store.state.user.info.id;
      this.$store.dispatch("getAllGroups", userId);
    },
    showCreateBar: function() {
      if (this.ownGroups.length >= 3) {
        this.$elementMessage("每人只能创建3个小组", "error", 1500);
        this.createBar = false;
        return;
      }
      this.createBar = true;
      document.addEventListener("click", this.clickListener);
    },
    clickListener(e) {
      let box = document.getElementsByClassName("group-create-bar")[0];
      if (box.contains(e.target)) return;
      this.createBar = false;
      document.removeEventListener("click", this.clickListener);
    },
    createGroup() {
      this.refreshGroups();
      if (this.ownGroups.length >= 3) {
        this.$elementMessage("每人只能创建3个小组", "error", 1500);
        this.createBar = false;
        return;
      }
      let _this = this;
      let userId = this.$store.state.user.info.id;
      let data = {
        createdBy: userId,
        name: this.newGroup.name,
        introduction: this.newGroup.introduction,
        icon: this.$store.state.user.info.icon
      };
      this.createBar = false;
      createGroup(data, userId)
        .then(function() {
          _this.refreshGroups();
        })
        .catch(function(err) {
          console.info("err:" + err);
        });
    },
    toGroupManager(id) {
      this.$router.push("/group/" + id + "/note");
    },
    //显示加入小组的消息框
    joinGroupDialog() {
      let _this = this;
      this.$alert(
        '<label style="display: block;">小组ID</label>' +
          '<input id="join-groupId" style="display: block; width: 100%; height: 25px; outline: none;"></input>' +
          '<label style="display: block;">验证码</label>' +
          '<input id="join-code" style="display: block; width: 100%; height: 25px; outline: none;"></input>',
        "加入小组",
        {
          dangerouslyUseHTMLString: true
        }
      )
        .then(function() {
          let userId = _this.userId;
          let groupId = document.getElementById("join-groupId").value;
          let code = document.getElementById("join-code").value;
          let idValid = "[0-9]+";
          if (!groupId.match(idValid)) {
            _this.$elementMessage("ID输入有误，ID只能是纯数字", "error", 1500);
            return;
          }
          joinGroupByCode(userId, groupId, code)
            .then(function() {
              _this.$store.dispatch("getAllGroups", userId);
              _this.$elementMessage("操作成功", "success", 1500);
            })
            .catch(err => {
              let e = err.message;
              if (e == null) e = "出错辣~请稍后重试";
              _this.$elementMessage(e, "error", 1500);
            });
        })
        .catch(function() {
          // _this.$elementMessage("请稍后重试", "error", 1500);
        });
    }
  }
};
</script>

<style scoped>
.group-wrap {
  display: block;
  text-align: left;
}

.group {
  position: relative;
  width: 300px;
  height: 400px;
  text-align: center;
  display: inline-block;
  background-color: #e8f2fe;
  margin: 20px 33px 20px 12px;
  padding: 15px 0;
  border-radius: 10px;
  box-sizing: border-box;
  cursor: pointer;
  overflow: hidden;
  -webkit-transition: 0.6s cubic-bezier(0.37, 1.44, 0.57, 0.77);
  transition: 0.6s cubic-bezier(0.37, 1.44, 0.57, 0.77);
}

.group:hover {
  transform: translateY(-2%);
  box-shadow: 1px 4px 10px 2px #ccc;
  -webkit-box-shadow: 1px 4px 10px 2px #ccc;
}

.group:hover .add {
  transform: scale3d(1.05, 1.05, 1) translate3d(0, -50%, 0);
  font-size: 100px;
}

.group:hover .avatar,
.group:hover .group-name {
  transform: scale3d(1.05, 1.05, 1);
}

.add {
  position: relative;
  width: 180px;
  height: 180px;
  border: 5px dashed #a2adc1;
  border-radius: 50px;
  line-height: 180px;
  font-size: 100px;
  color: #a2adc1;
  top: 50%;
  margin: 0 auto;
  transform: translate3d(0, -50%, 0);
}

.admin {
  font-weight: bold;
  font-size: 23px;
  position: absolute;
  top: 20px;
  left: 25px;
  color: rgba(231, 175, 47, 0.8);
}

.avatar {
  position: relative;
  width: 80px;
  height: 80px;
  display: block;
  margin: 10px auto;
}

.avatar img {
  position: relative;
  object-fit: cover;
  width: 100%;
  height: 100%;
  /*border: 1px solid #ddd;*/
  border-radius: 50%;
}

.group-name {
  font-weight: bold;
  font-size: 20px;
  padding: 5px;
}

.group-data {
  width: calc(100% - 50px);
  display: flex;
  height: 65px;
  margin: 5px 0;
  padding-left: 25px;
  padding-right: 25px;
}

.data-cell {
  flex: 1;
  height: 100%;
  text-align: center;
  padding: 5px;
  box-sizing: border-box;
  overflow: hidden;
}

.data-cell label {
  font-size: 15px;
  color: #a2adc1;
  cursor: pointer;
}

.data-cell p {
  margin-top: 10px;
}

.data-cell:hover p {
  font-weight: bold;
}

.group-introduction {
  text-align: left;
  height: 150px;
  overflow: hidden;
  position: relative;
  /*transition: 0.5s;*/
  word-break: break-word;
  padding: 0 15px 0 15px;
  -webkit-transition: 0.5s cubic-bezier(0.37, 1.44, 0.57, 0.77);
  transition: 0.5s cubic-bezier(0.37, 1.44, 0.57, 0.77);
}

.group-introduction p {
  white-space: pre-wrap;
  word-wrap: break-word;
  word-break: break-all;
}

.group-introduction:hover {
  border-radius: 20px 20px 0 0;
  position: relative;
  z-index: 100;
  height: 400px;
  background-color: #fff;
  /*background-color: #d1e7fe;*/
  bottom: 5px;
  transform: translate3d(0, -130px, 0);
}

.group-create {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1000;
  background-color: rgba(255, 255, 255, 0.8);
}

.group-create-bar {
  display: block;
  position: relative;
  top: 50%;
  left: 50%;
  transform: translate3d(-50%, -50%, 0);
  background-color: #e8f2fe;
  width: 300px;
  height: 300px;
  border-radius: 15px;
  padding: 20px;
  box-shadow: 1px 4px 10px 2px #ccc;
  -webkit-box-shadow: 1px 4px 10px 2px #ccc;
}

.group-create-bar-delete {
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

.create-introduction {
  margin-bottom: 20px;
}

.create-introduction input {
  height: 500px;
}
</style>
