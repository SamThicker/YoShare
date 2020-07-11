import request from "../../static/utils/request";

export function addReadNoteLog(userId, noteId, noteType, groupId, title) {
  return request({
    url: "/log/readNoteLog/" + userId,
    method: "post",
    params: {
      noteId: noteId,
      noteType: noteType,
      groupId: groupId,
      title: title
    }
  });
}

export function queryReadNoteLog(userId, option) {
  return request({
    url: "/log/readNoteLog/" + userId + "/" + option,
    method: "get"
  });
}

export function queryReadNoteDetail(userId, date) {
  console.info(date)
  return request({
    url: "/log/readNoteLogDetails/" + userId,
    method: "post",
    params: {
      dateStr: date
    }
  });
}
