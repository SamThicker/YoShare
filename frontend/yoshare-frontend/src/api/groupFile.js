import corsRequest from "../../static/utils/corsRequest";
import request from "../../static/utils/request";

export function uploadFile(url, file) {
  let headers = {
    "Content-Type": file.type
  };
  return corsRequest({
    url: url,
    method: "put",
    data: file,
    headers: headers
  });
}

export function getUploadUrl(fileName) {
  return request({
    url: "/file/uploadUrl/" + fileName,
    method: "get"
  });
}

export async function uploadDirectly(file) {
  let url = (await getUploadUrl(file.name)).data;
  return uploadFile(url, file);
}

export function getBlogFilePutUrl(userId, filename) {
  return request({
    url: "/file/blogFilePutUrl/" + userId + "/" + filename,
    method: "get"
  });
}

export function uploadFileToServer(userId, formData) {
  return request({
    url: "/zuul/file/member/" + userId + "/file",
    method: "put",
    headers: {
      "content-type": "application/x-www-form-urlencoded"
    },
    data: formData
  });
}

export function uploadExistFileToServer(userId, formData) {
  return request({
    url: "/file/member/" + userId + "/existFile",
    method: "put",
    headers: {
      "content-type": "application/x-www-form-urlencoded"
    },
    data: formData
  });
}
