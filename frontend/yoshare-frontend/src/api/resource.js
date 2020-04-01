import request from "../../static/utils/request";

export function getOwnResource(id) {
  return request({
    url: "resource/" + id + "/ownResource",
    method: "get"
  });
}

export function delResourceNote(userId, resource) {
  return request({
    url: "resource/" + userId + "/ownResource",
    method: "delete",
    data: resource
  });
}
