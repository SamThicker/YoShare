import request from "../../static/utils/request";

export function getArea(param, option) {
  return request({
    url: "/member/area/" + param + "/" + option,
    method: "get"
  });
}
