<template>
  <div class="group-manager-wrap">
    <!-- 侧边栏 -->
    <div class="nav-sidebar">
      <!--顶部区域-->
      <div class="top-area">
        <i class="el-icon-s-home home" @click="backHome()"></i>
      </div>
      <!--中部区域-->
      <div class="middle-area">
        <ul>
          <li>
            <div class="nav-item active" @click="select(0, 'note')">
              <span class="icon"
                ><i class="el-icon-edit hollow"></i
                ><i class="el-icon-edit-outline solid"></i
              ></span>
              <span class="message">笔记</span>
            </div>
          </li>
          <li>
            <div class="nav-item" @click="select(1, 'file')">
              <span class="icon"
                ><i class="el-icon-folder hollow"></i
                ><i class="el-icon-folder-opened solid"></i
              ></span>
              <span class="message">文件</span>
            </div>
          </li>
          <li>
            <div class="nav-item" @click="select(2, 'favorite')">
              <span class="icon"
                ><i class="el-icon-star-off hollow"></i
                ><i class="el-icon-star-on solid"></i
              ></span>
              <span class="message">收藏</span>
            </div>
          </li>
          <li>
            <div class="nav-item" @click="select(3, 'member')">
              <span class="icon"
                ><i class="el-icon-user hollow"></i
                ><i class="el-icon-user-solid solid"></i
              ></span>
              <span class="message">组员</span>
            </div>
          </li>
          <li>
            <div class="nav-item" @click="select(4, 'setting')">
              <span class="icon">
                <!--<i class="el-icon-setting hollow" style="color: #fff"></i>-->
                <!--<i class="el-icon-user hollow"></i>-->
                <i class="el-icon-s-tools hollow"></i>
                <i class="el-icon-s-tools solid"></i>
              </span>
              <span class="message">设置</span>
            </div>
          </li>
        </ul>
      </div>

      <!--底部区域-->
      <div
        class="bottom-area"
        @click="console.info(JSON.stringify(this.currentGroup))"
      >
        <el-tooltip
          class="item"
          effect="dark"
          :content="currentGroup ? currentGroup.name : null"
          placement="right-end"
        >
          <div class="avatar-wrap">
            <img
              :src="getIconDir(currentGroup ? currentGroup.icon : null)"
              class="group-avatar"
              @click.stop="showGroupList()"
            />
          </div>
        </el-tooltip>
      </div>

      <!--小组列表-->
      <transition name="el-fade-in">
        <div v-show="groupList" class="group-list">
          <ul>
            <li
              v-show="!(parseInt(group.id) === parseInt(currentGroupId))"
              v-for="group in groups"
              :key="group.id"
              @click="selectGroup(group ? group.id : null)"
            >
              <span class="group-list-avatar">
                <img :src="group ? getIconDir(group.icon) : ''" />
              </span>
              <span class="group-list-name">
                <div>
                  {{ group.name }}
                </div>
              </span>
            </li>
          </ul>
        </div>
      </transition>
    </div>

    <!-- 内容面板 -->
    <div class="content">
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  name: "GroupManage",
  mounted() {
    this.$store.dispatch("getOwnGroups", this.userId);
    //根据url设置当前选中的选项，提高用户刷新页面时的体验
    let pathParts = this.$route.path.split("/");
    let option = pathParts[pathParts.length - 1];
    let tab = ["note", "resource", "favorite", "member", "setting"];
    tab.forEach((val, index) => {
      if (val === option) {
        this.select(index, option);
      }
    });
  },
  destroyed() {
    document.removeEventListener("click", this.clickListener);
  },
  watch: {
    currentGroup: function() {},
    userId: function(val) {
      this.$store.dispatch("getOwnGroups", val);
    },
    groups: function(groups) {
      //为了通过eslint的操作符号检查，把两个值改为int，使用"==="严格判断
      let groupId = parseInt(this.currentGroupId);
      groups.forEach(group => {
        let expected = parseInt(group.id);
        if (expected === groupId) {
          this.currentGroup = group;
        }
      });
    },
    deep: true
  },
  data() {
    return {
      currentGroup: null,
      groupList: false
    };
  },
  computed: {
    otherGroups() {
      let current = parseInt(this.currentGroupId);
      return this.groups.filter(group => {
        let id = parseInt(group.id);
        return id !== current;
      });
    },
    groups() {
      return this.$store.state.user.allGroups;
    },
    userId() {
      return this.$store.state.user.info.id;
    },
    currentGroupId() {
      return this.$route.params.id;
    }
  },
  methods: {
    backHome() {
      this.$router.push("/");
    },
    select(index, option) {
      let els = document.querySelectorAll(".nav-item");
      for (let el of els) {
        el.classList.remove("active");
      }
      els[index].classList.add("active");
      let id = this.$route.params.id;
      let path = "/group/" + id + "/" + option;
      if (path === this.$route.path) return;
      this.$router.push(path);
    },
    showGroupList() {
      this.groupList = !this.groupList;
      if (this.groupList) {
        document.addEventListener("click", this.clickListener);
      } else {
        document.removeEventListener("click", this.clickListener);
      }
    },
    clickListener(e) {
      let el = document.querySelector(".group-list");
      if (!el.contains(e.target)) {
        this.groupList = false;
        document.removeEventListener("click", this.clickListener);
      }
    },
    getIconDir(icon) {
      if (icon) {
        return "http://localhost" + icon;
      }
      return "http://localhost/static/icon/default.png";
    },
    selectGroup(id) {
      this.groupList = false;
      if (!id) return;
      //跳转到相应小组的页面
      let path = this.$route.path;
      let pathParts = path.split("/");
      pathParts[2] = id;
      path = "";
      pathParts.forEach(part => (path += "/" + part));
      path = path.replace("/", "");
      if (path === this.$route.path) return;
      this.currentGroup = this.groups.filter(
        group => parseInt(group.id) === parseInt(id)
      );
      this.currentGroup = this.currentGroup[0];
      this.$router.push(path);
    }
  }
};
</script>

<style scoped>
ul,
li {
  padding: 0;
  margin: 0;
  list-style: none;
}

.group-manager-wrap {
  width: 100%;
  height: 100%;
  display: flex;
}

.nav-sidebar {
  position: relative;
  top: 0;
  left: 0;
  width: 70px;
  height: 100%;
  background-color: #7bb6fb;
  box-sizing: border-box;
}

.top-area {
  cursor: pointer;
  height: 100px;
}

.top-area:hover .home {
  color: rgba(255, 255, 255, 0.7);
}

.home {
  line-height: 100px;
  font-size: 35px;
  color: #fff;
}

.middle-area {
  height: calc(100% - 250px);
  width: 100%;
  display: flex;
  flex: 1;
}

.middle-area ul {
  height: 100%;
  width: 100%;
  display: block;
}

.middle-area ul li {
  position: relative;
  width: 100%;
  height: 70px;
  box-sizing: border-box;
}

.nav-item {
  position: relative;
  width: 100%;
  height: 70px;
  cursor: pointer;
  overflow: hidden;
  color: #fff;
}

.nav-item.active::before {
  content: "";
  border-top: 6px solid transparent;
  border-right: 5px solid #fdfdfd;
  border-bottom: 6px solid transparent;
  position: absolute;
  right: 0;
  top: 30px;
}

.nav-item:hover {
  background-color: rgba(0, 0, 0, 0.1);
}

.hollow {
  transition: 0.3s;
}

.solid {
  font-size: 32px;
  display: none;
  transition: 0.3s;
}

.nav-item:hover .solid {
  display: block;
}

.nav-item:hover .hollow {
  display: none;
}

.nav-item .icon {
  display: block;
  font-size: 28px;
  transform: translate3d(0, 17px, 0);
  transition: 0.3s;
}

.nav-item .message {
  font-size: 12px;
  display: none;
  transform: translate3d(0, 11px, 0);
  transition: 0.3s;
}

.nav-item:hover .message {
  display: block;
}

.nav-item:hover .icon {
  transform: translate3d(0, 7px, 0);
}

.bottom-area {
  width: 100%;
  height: 150px;
  position: absolute;
  display: block;
  bottom: 0;
}

.avatar-wrap {
  height: 60px;
  width: 60px;
  position: absolute;
  bottom: 8px;
  left: 50%;
  transform: translate3d(-50%, 0, 0);
  cursor: pointer;
}

.group-avatar {
  width: 100%;
  height: 100%;
  border-radius: 100%;
  box-sizing: border-box;
  object-fit: cover;
}

.group-list {
  z-index: 100;
  width: 300px;
  height: 500px;
  position: relative;
  background-color: #e8f2fe;
  right: calc(-100% - 3px);
  bottom: calc(500px - 145px);
  border-radius: 10px;
  padding: 10px;
  box-sizing: border-box;
  box-shadow: 1px 4px 10px 2px #ccc;
  -webkit-box-shadow: 1px 4px 10px 2px #ccc;
}

.group-list ul li {
  margin: 7px 0;
  padding: 3px 5px;
  height: 50px;
  line-height: 50px;
  border-radius: 5px;
  text-align: left;
  overflow-y: hidden;
  box-sizing: border-box;
  display: flex;
  background-color: #d1e7fe;
  cursor: pointer;
}

.group-list ul li:hover {
  background-color: #bbdafd;
}

.group-list-avatar {
  line-height: 50px;
  width: 42px;
  height: 42px;
  background-color: #7bb6fb;
  margin: auto 5px;
  display: inline-block;
  border-radius: 100%;
}

.group-list-avatar img {
  width: 100%;
  height: 100%;
  border-radius: 100%;
  object-fit: cover;
}

.group-list-name {
  box-sizing: border-box;
  line-height: 45px;
  display: inline-block;
  margin: auto auto auto 5px;
  font-size: 18px;
  text-align: left;
  flex: 1;
}

.content {
  width: 100%;
  flex: 1;
  height: 100%;
}
</style>
