import corsRequest from "../../static/utils/corsRequest";
import request from "../../static/utils/request";

export function uploadFile(url, file, headers) {
  console.info("uploading");
  return corsRequest({
    url: url,
    method: "put",
    data: file,
    headers: headers
  });
}

export function getUploadUrl(fileName) {
  return request({
    url: "/resource/uploadUrl/" + fileName,
    method: "get"
  });
}

export async function uploadDirectly(file) {
  let url = (await getUploadUrl(file.name)).data;
  return uploadFile(url, file);
}
