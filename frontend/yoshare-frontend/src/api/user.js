import request from "../../static/utils/request";

export function login(account, password) {
  return request({
    url: "/security/member/login",
    method: "post",
    data: {
      account: account,
      password: password
    }
  });
}

export function getInfo(param, option) {
  return request({
    url: "/member/" + param + "/info/" + option,
    method: "get"
  });
}

export function getInfoByToken() {
  return request({
    url: "/gateway/security/info",
    method: "get"
  });
}

export function register(registerInfo) {
  return request({
    url: "/member/register",
    method: "post",
    data: registerInfo,
    headers: {
      "Content-Type": "application/json"
    }
  });
}

export function updateInfo(id, info) {
  return request({
    url: "/member/" + id + "/info",
    method: "put",
    data: info
  });
}

export function getVerificationCode(mail) {
  return request({
    url: "/member/mailVerificationCode",
    method: "post",
    params: { mail: mail }
  });
}
