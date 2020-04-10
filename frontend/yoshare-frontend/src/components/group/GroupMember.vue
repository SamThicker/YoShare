<template>
  <div class="user-panel">
    <transition-group name="el-fade-in">
      <div class="user-card" v-for="member in groupMembers" :key="member.id">
        <div class="user-avatar">
          <img :src="'http://localhost/static/icon/' + member.icon" />
        </div>
        <div class="user-name">{{ member.nickname }}</div>
        <div class="user-data">
          <div class="data-cell">
            <label>ID</label>
            <p>{{ member.id }}</p>
          </div>
          <div class="data-cell">
            <label>GENDER</label>
            <p>
              <i
                style="color: blue"
                class="el-icon-male"
                v-if="member.gender === '1'"
              ></i>
              <i
                style="color: deeppink"
                class="el-icon-female"
                v-else-if="member.gender === '0'"
              ></i>
              <i style="color: purple" class="el-icon-s-custom" v-else></i>
            </p>
          </div>
        </div>
        <label class="location-label">地区</label>
        <p class="location-content">{{ member.location }}</p>
        <div class="user-introduction">
          <p>
            {{ member.introduction }}
          </p>
        </div>
      </div>
    </transition-group>

    <!--添加成员-->
    <transition name="el-fade-in">
      <div
        class="user-card"
        @click="addMember()"
        v-if="groupMembers.length < 5"
      >
        <div class="add">+</div>
      </div>
    </transition>

    <!--加入小组验证码对话框-->
    <el-dialog
      :close-on-click-modal="false"
      class="join-group"
      title="操作指引"
      :visible.sync="dialogTableVisible"
    >
      <div class="add-member-instruction">{{ instruction }}</div>
      <label class="join-label">小组ID</label>
      <span class="join-data">{{ groupId }}</span>
      <label class="join-label">验证码</label>
      <span class="join-data"> {{ code }} </span>
    </el-dialog>
  </div>
</template>

<script>
import { getAllGroupMember, getGroupJoinCode } from "../../api/group";

export default {
  name: "GroupMember",
  mounted() {
    this.getGroupMembers();
  },
  data() {
    return {
      user: this.userInfo,
      groupMembers: [],
      dialogTableVisible: false,
      code: "",
      instruction:
        '       请把验证码与小组ID分享给您的小伙伴，您的小伙伴只需在"小组"内容面板下点击' +
        '"加入小组"的小卡片，然后输入验证码和小组ID，即可加入小组。 验证码有效期10分钟。'
    };
  },
  watch: {
    groupId: function() {
      this.getGroupMembers();
    }
  },
  computed: {
    userInfo: function() {
      return this.$store.state.user.info;
    },
    userId: function() {
      return this.$store.state.user.info.id;
    },
    groupId: function() {
      return this.$route.params.groupId;
    },
    getIcon: function(icon) {
      return "http://localhost/" + icon;
    }
  },
  methods: {
    search(val) {
      console.info(val);
    },
    getGroupMembers() {
      let _this = this;
      getAllGroupMember(this.userId, this.groupId)
        .then(function(res) {
          _this.groupMembers = res.data;
        })
        .catch(function(err) {
          console.info("err:" + err.data.message);
        });
    },
    addMember() {
      let _this = this;
      getGroupJoinCode(this.userId, this.groupId)
        .then(function(res) {
          let code = res.data;
          _this.code = code;
          _this.dialogTableVisible = true;
        })
        .catch(err => {
          console.info("err:" + err);
        });
    }
  }
};
</script>

<style scoped>
.user-panel {
  padding: 20px;
  display: -webkit-box;
  height: 100%;
  box-sizing: border-box;
  overflow: auto;
}

.user-card {
  width: 300px;
  height: 400px;
  border-radius: 10px;
  background-color: #e8f2fe;
  display: inline-block;
  box-sizing: border-box;
  cursor: pointer;
  overflow: hidden;
  margin: 20px;
  -webkit-transition: 0.6s cubic-bezier(0.37, 1.44, 0.57, 0.77);
  transition: 0.6s cubic-bezier(0.37, 1.44, 0.57, 0.77);
}

.user-card:hover {
  transform: translateY(-2%);
  box-shadow: 1px 4px 10px 2px #ccc;
  -webkit-box-shadow: 1px 4px 10px 2px #ccc;
}

.user-avatar {
  width: 100px;
  height: 100px;
  border-radius: 100%;
  margin: 20px auto;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  border-radius: 100%;
  object-fit: cover;
}

.user-name {
  font-weight: bold;
  font-size: 20px;
  padding: 3px;
}

.user-data {
  width: calc(100% - 50px);
  display: flex;
  height: 50px;
  margin: 5px 0;
  padding-left: 25px;
  padding-right: 25px;
  text-align: center;
}

.data-cell {
  flex: 1;
  height: 100%;
  text-align: center;
  padding: 2px;
  box-sizing: border-box;
  overflow: hidden;
}

.data-cell label {
  font-size: 10px;
  color: #a2adc1;
  width: 100%;
  text-align: center;
}

.data-cell p {
  margin-top: 5px;
  font-size: 12px;
  width: 100%;
  text-align: center;
}

.location-label {
  position: relative;
  font-size: 10px;
  color: #a2adc1;
  display: inline-block;
  width: 50px;
  float: left;
  left: 10px;
}

.location-content {
  display: inline-block;
  width: calc(100% - 60px);
  font-size: 13px;
  box-sizing: border-box;
  overflow-x: auto;
  float: left;
  text-align: center;
  margin: 0;
}

.user-introduction {
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

.user-introduction p {
  white-space: pre-wrap;
  word-wrap: break-word;
  word-break: break-all;
}

.user-introduction:hover {
  border-radius: 20px 20px 0 0;
  position: relative;
  z-index: 100;
  height: 400px;
  background-color: #fff;
  bottom: 5px;
  transform: translate3d(0, -130px, 0);
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
.user-card:hover .add {
  transform: scale3d(1.05, 1.05, 1) translate3d(0, -50%, 0);
  font-size: 100px;
}

.join-group p {
  word-break: break-word;
  word-wrap: break-word;
}

.join-label {
  display: block;
  padding-top: 20px;
}

.join-data {
  display: block;
  padding-bottom: 20px;
  font-size: 20px;
  font-weight: bold;
  color: orange;
}

.add-member-instruction {
  word-break: break-word;
  word-wrap: break-word;
  white-space: pre-wrap;
  text-align: left;
}
</style>
