import Vue from "vue";
import Vuex from "vuex";
import user from "./modules/user";
import getters from "./getters";

Vue.use(Vuex);
const store = new Vuex.Store({
  modules: {
    user: user
  },
  getters: getters,
  strict: true
});

export default store;
