import request from "../../static/utils/request";

export function createGroup(data, userId) {
  return request({
    url: "/group/" + userId + "/group",
    method: "put",
    data: data
  });
}

export function getOwnGroupsByUserId(userId) {
  return request({
    url: "/group/" + userId + "/createdGroups",
    method: "get"
  });
}

export function getAllGroupsByUserId(userId) {
  return request({
    url: "/group/" + userId + "/allGroups",
    method: "get"
  });
}

export function getAvatarUploadUrl(userId, groupId, type) {
  return request({
    url: "/group/" + userId + "/" + groupId + "/avatarUploadUrl",
    method: "get",
    params: {
      type: type
    }
  });
}
