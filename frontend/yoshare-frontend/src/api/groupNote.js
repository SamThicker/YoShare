import request from "../../static/utils/request";

export function createGroupNote(groupId, data) {
  return request({
    url: "/note/group/note" + groupId,
    method: "post",
    data: data
  });
}

export function getGroupNote(groupId, noteId) {
  return request({
    url: "/note/group/getNote/" + groupId + "/" + noteId,
    method: "get"
  });
}

export function saveGroupNote(groupId, data) {
  let url = "/note/group/updateNote/" + groupId;
  return request({
    url: url,
    method: "post",
    data: data
  });
}
