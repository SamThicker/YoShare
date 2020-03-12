const getters = {
  token: state => state.user.token,
  nickname: state => state.user.nickname,
  username: state => state.user.username,
  icon: state => state.user.icon,
  account: state => state.user.account,
  info: state => state.user.info
};

export default getters;
