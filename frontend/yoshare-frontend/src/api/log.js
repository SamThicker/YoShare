import request from "../../static/utils/request";

export function addReadNoteLog(userId, noteId) {
  return request({
    url: "/log/readNoteLog/" + userId,
    method: "post",
    params: {
      noteId: noteId
    }
  });
}

export function queryReadNoteLog(userId, option) {
  return request({
    url: "/log/readNoteLog/" + userId + "/" + option,
    method: "get"
  });
}
