import request from "../../static/utils/request";

export function getOwnResource(id, type) {
  if (!type) type = "all";
  return request({
    url: "resource/" + id + "/ownResource",
    method: "get",
    params: {
      type: type
    }
  });
}

export function delResourceNote(userId, resource) {
  return request({
    url: "resource/" + userId + "/ownResource",
    method: "delete",
    data: resource
  });
}

export function addMemClassification(userId, type, name) {
  return request({
    url: "resource/" + userId + "/ownResource/classification",
    method: "put",
    params: {
      type: type,
      name: name
    }
  });
}

export function getMemClassification(userId, type) {
  return request({
    url: "resource/" + userId + "/ownResource/classification",
    method: "get",
    params: {
      type: type
    }
  });
}

export function updateMemClassification(userId, classification) {
  return request({
    url: "resource/" + userId + "/ownResource/classification",
    method: "patch",
    data: classification
  });
}

export function deleteMemClassification(userId, classificationId) {
  return request({
    url: "resource/" + userId + "/ownResource/classification",
    method: "delete",
    params: {
      classificationId: classificationId
    }
  });
}

export function addMemFavPage(userId, title, introduction, url) {
  return request({
    url: "resource/" + userId + "/ownResource/web",
    method: "put",
    params: {
      userId: userId,
      title: title,
      introduction: introduction,
      url: url
    }
  });
}

export function getMemFavPage(userId, webId) {
  return request({
    url: "resource/" + userId + "/ownResource/web",
    method: "get",
    params: {
      webId: webId
    }
  });
}
