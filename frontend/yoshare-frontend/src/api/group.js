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
