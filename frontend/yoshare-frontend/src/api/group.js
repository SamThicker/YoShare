import request from "../../static/utils/request";

export function createGroup(data, userId) {
  return request({
    url: "/group-service/group/" + userId + "/group",
    method: "put",
    data: data
  });
}

export function updateGroupInfo(userId, group) {
  return request({
    url: "/group-service/group/" + userId + "/info",
    method: "patch",
    data: group
  });
}

export function getOwnGroupsByUserId(userId) {
  return request({
    url: "/group-service/group/" + userId + "/createdGroups",
    method: "get"
  });
}

export function getAllGroupsByUserId(userId) {
  return request({
    url: "/group-service/group/" + userId + "/allGroups",
    method: "get"
  });
}

export function getAllGroupMember(userId, groupId) {
  return request({
    url: "/group-service/group/" + userId + "/group/member",
    method: "get",
    params: {
      groupId: groupId
    }
  });
}

export function getAvatarUploadUrl(userId, groupId, type) {
  return request({
    url: "/group-service/group/" + userId + "/" + groupId + "/avatarUploadUrl",
    method: "get",
    params: {
      type: type
    }
  });
}

export function getGroupJoinCode(userId, groupId) {
  return request({
    url: "/group-service/group/" + userId + "/groupCode",
    method: "get",
    params: {
      groupId: groupId
    }
  });
}

export function joinGroupByCode(userId, groupId, groupJoinCode) {
  return request({
    url: "/group-service/group/" + userId + "/group/member",
    method: "put",
    params: {
      groupId: groupId,
      groupJoinCode: groupJoinCode
    }
  });
}
