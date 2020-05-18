import request from "../../static/utils/request";
import rawRequest from "../../static/utils/rawRequest";

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
    url: "/member-service/member/" + param + "/info/" + option,
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
    url: "/member-service/member/register",
    method: "post",
    data: registerInfo,
    headers: {
      "Content-Type": "application/json"
    }
  });
}

export function updateInfo(id, info) {
  return request({
    url: "/member-service/member/" + id + "/info",
    method: "put",
    data: info
  });
}

export function getVerificationCode(mail) {
  return request({
    url: "/member-service/member/mailVerificationCode",
    method: "post",
    params: { mail: mail }
  });
}

export function getAvatarUploadUrl(userId, type) {
  return request({
    url: "/member-service/member/" + userId + "/avatarUploadUrl",
    method: "get",
    params: {
      type: type
    }
  });
}

export function refreshAvatar(url) {
  return rawRequest({
    url: url,
    method: "get",
    headers: {
      "Cache-Control": "no-cache"
    }
  });
}

export function refreshAvatarNew(url) {
  return request({
    url: url,
    method: "get",
    headers: {
      "Cache-Control": "no-cache"
    }
  });
}
