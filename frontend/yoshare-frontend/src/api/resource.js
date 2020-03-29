import request from "../../static/utils/request";

export function getOwnResource(id) {
  return request({
    url: "resource/" + id + "/ownResource",
    method: "get"
  });
}
