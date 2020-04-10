<template>
  <div class="search-wrap">
    <div class="user-panel">
      <div class="user-card">
        <div class="user-avatar">
          <img :src="'http://localhost' + userInfo.icon" />
        </div>
        <div class="user-name">{{ userInfo.nickname }}</div>
        <div class="user-data">
          <div class="data-cell">
            <label>ID</label>
            <p>{{ userInfo.id }}</p>
          </div>
          <div class="data-cell">
            <label>GENDER</label>
            <p>{{ userInfo.gender }}</p>
          </div>
        </div>
        <div class="user-introduction">
          <p>
            {{ userInfo.introduction }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Search",
  mounted() {
    this.search(this.searchWD);
  },
  data() {
    return {
      user: this.userInfo
    };
  },
  watch: {
    searchWD: function(val) {
      this.search(val);
    }
  },
  computed: {
    userInfo: function() {
      return this.$store.state.user.info;
    },
    searchWD: function() {
      return this.$store.state.globalEnv.searchWD;
    }
  },
  methods: {
    search(val) {
      console.info(val);
    }
  }
};
</script>

<style scoped>
/*.search-wrap {*/
/*  width: 100%;*/
/*  height: 100%;*/
/*  background-color: #ff6464;*/
/*}*/

.user-panel {
  width: 80%;
  height: auto;
  min-width: 300px;
  min-height: 50px;
  margin: 20px auto;
  display: -webkit-box;
  padding: 20px;
  box-sizing: border-box;
}

.user-card {
  width: 300px;
  height: 400px;
  border-radius: 10px;
  background-color: #e8f2fe;
  box-sizing: border-box;
  cursor: pointer;
  overflow: hidden;
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
  padding: 5px;
}

.user-data {
  width: calc(100% - 50px);
  display: flex;
  height: 65px;
  margin: 5px 0;
  padding-left: 25px;
  padding-right: 25px;
  text-align: center;
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
  width: 100%;
  text-align: center;
}

.data-cell p {
  margin-top: 10px;
  width: 100%;
  text-align: center;
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
  /*background-color: #d1e7fe;*/
  bottom: 5px;
  transform: translate3d(0, -130px, 0);
}
</style>
