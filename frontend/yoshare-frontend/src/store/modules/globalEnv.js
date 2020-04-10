const globalEnv = {
  state: {
    searchWD: ""
  },

  mutations: {
    SET_SEARCHWD: (state, word) => {
      state.searchWD = word;
    }
  },
  actions: {}
};

export default globalEnv;
