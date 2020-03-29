import request from "../../static/utils/request";

export function createNote(userId, data) {
  return request({
    url: "/note/addNote/" + userId,
    method: "post",
    data: data
  });
}

export function getNoteForSelf(userId, noteId) {
  return request({
    url: "/note/getNote/" + userId + "/" + noteId,
    method: "get"
  });
}

export function saveNote(userId, data) {
  return request({
    url: "/note/updateNote/" + userId,
    method: "post",
    data: data
  });
}
