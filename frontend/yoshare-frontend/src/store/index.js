import Vue from "vue";
import Vuex from "vuex";
import user from "./modules/user";
import globalEnv from "./modules/globalEnv";
import getters from "./getters";

Vue.use(Vuex);
const store = new Vuex.Store({
  modules: {
    user: user,
    globalEnv: globalEnv
  },
  getters: getters,
  strict: true
});

export default store;
