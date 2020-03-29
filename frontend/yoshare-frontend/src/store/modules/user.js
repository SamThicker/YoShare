import { login, getInfoByToken } from "@/api/user";
import { getToken, setToken, removeToken } from "../../../static/utils/auth";
import { getOwnGroupsByUserId } from "../../api/group";

const user = {
  state: {
    token: getToken(),
    nickname: "",
    username: "",
    icon: "",
    account: "",
    info: {
      id: "",
      username: "",
      nickname: "",
      icon: "",
      birthday: "",
      gender: "",
      introduction: "",
      location: "",
      registeredTime: ""
    },
    ownGroups: [],
    joinGroups: [],
    allGroups: new Set()
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token;
    },
    SET_NICKNAME: (state, nickname) => {
      state.nickname = nickname;
    },
    SET_ICON: (state, icon) => {
      state.icon = icon;
    },
    SET_USERNAME: (state, username) => {
      state.username = username;
    },
    SET_ACCOUNT: (state, account) => {
      state.account = account;
    },
    SET_INFO: (state, info) => {
      state.info = info;
    },
    SET_OWNGROUPS: (state, groups) => {
      state.ownGroups = groups;
    },
    SET_JOINGROUPS: (state, groups) => {
      state.joinGroups = groups;
    },
    SET_ALLGROUPS: (state, groups) => {
      state.allGroups = groups;
    }
  },

  actions: {
    //登录
    Login({ dispatch, commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo.account, userInfo.password)
          .then(response => {
            const data = response.data;
            const tokenStr = data.token;
            setToken(tokenStr);
            commit("SET_TOKEN", tokenStr);
            dispatch("GetInfoByToken");
            resolve(response);
          })
          .catch(error => {
            reject(error);
          });
      });
    },

    // //获取用户数据
    // GetInfo({ commit }, params) {
    //   return new Promise((resolve, reject) => {
    //     getInfo(params.param, params.option)
    //       .then(response => {
    //         const data = response.data;
    //         commit("SET_NICKNAME", data.nickname);
    //         commit("SET_USERNAME", data.username);
    //         commit("SET_ICON", data.icon);
    //         resolve(response);
    //       })
    //       .catch(error => {
    //         reject(error);
    //       });
    //   });
    // },

    // //获取用户数据
    GetInfoByToken({ commit }) {
      if (!this.state.user.token) return;
      return new Promise((resolve, reject) => {
        getInfoByToken()
          .then(response => {
            const data = response.data;
            commit("SET_ACCOUNT", data.username);
            commit("SET_NICKNAME", data.nickname);
            commit("SET_USERNAME", data.username);
            commit("SET_ICON", data.icon);
            commit("SET_INFO", data);
            resolve(response);
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    //登出
    LogOut({ commit }) {
      return new Promise(resolve => {
        commit("SET_TOKEN", "");
        commit("SET_NICKNAME", "");
        commit("SET_USERNAME", "");
        commit("SET_ICON", "");
        commit("SET_ACCOUNT", "");
        removeToken();
        resolve();
      });
    },
    getOwnGroups({ commit }, userId) {
      return new Promise((resolve, reject) => {
        getOwnGroupsByUserId(userId)
          .then(function(res) {
            commit("SET_OWNGROUPS", res.data);
            commit("SET_ALLGROUPS", res.data);
            resolve();
          })
          .catch(function(err) {
            console.info("err:" + err);
            reject();
          });
      });
    }
  }
};

export default user;
