import request from "../../static/utils/request";
import corsRequestPro from "../../static/utils/corsRequestPro";

// export function uploadFileToServer(groupId, formData) {
//   return request({
//     url: "/zuul/file/group/" + groupId + "/file",
//     method: "put",
//     headers: {
//       "content-type": "application/x-www-form-urlencoded"
//     },
//     data: formData
//   });
// }
export function uploadFileToServer(groupId, formData) {
  return request({
    url: "/file/group/" + groupId + "/file",
    method: "put",
    headers: {
      "content-type": "application/x-www-form-urlencoded"
    },
    data: formData
  });
}

export function uploadExistFileToServer(groupId, formData) {
  return request({
    url: "/file/group/" + groupId + "/existFile",
    method: "put",
    headers: {
      "content-type": "application/x-www-form-urlencoded"
    },
    data: formData
  });
}

export function downloadFile(fileId) {
  return request({
    url: "/file/member/file/" + fileId,
    method: "get",
    headers: {
      responseType: "blob"
    }
  });
}

export function getFileInfo(groupId, fileId) {
  return request({
    url: "/file/group/" + groupId + "/fileInfo/" + fileId,
    method: "get"
  });
}

export function download(url) {
  return corsRequestPro({
    url: url,
    method: "get",
    headers: {
      "Content-Disposition": "attachment"
      // origin: "127.0.0.1"
    }
  });
}
