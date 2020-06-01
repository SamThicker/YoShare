// import corsRequest from "../../static/utils/corsRequest";
import request from "../../static/utils/request";

//尝试上传，如果后台发现一致md5则秒传成功
export function tryUpload(formData, id, option) {
  let url = "";
  if (option === "MEMBER") {
    url = "/file/member/" + id + "/existFile";
  } else {
    url = "/file/group/" + id + "/existFile";
  }
  return request({
    url: url,
    data: formData,
    method: "put"
  });
}

//获取上一次上传进度，如果没有相应记录，则从头开始上传
export function readyForUpload(formData, id, option) {
  let url = "";
  if (option === "MEMBER") {
    url = "/file/member/" + id + "/file";
  } else {
    url = "/file/group/" + id + "/file";
  }
  return request({
    url: url,
    method: "put",
    data: formData
  });
}

// 上传分片
export function doUpload(formData, id, option) {
  let url = "";
  if (option === "MEMBER") {
    url = "/file/member/multipartFile";
  } else {
    url = "/file/group/" + id + "/multipartFile";
  }
  return request({
    url: url,
    data: formData,
    method: "post",
    headers: {
      "content-type": "application/x-www-form-urlencoded"
    }
  });
}
