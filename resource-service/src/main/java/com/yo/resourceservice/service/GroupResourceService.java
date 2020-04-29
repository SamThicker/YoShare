package com.yo.resourceservice.service;

import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.model.CmsGroupResource;
import com.yo.yoshare.mbg.model.CmsMemberResource;
import com.yo.yoshare.mbg.model.CmsMemberResourceClassfication;

import java.util.List;

public interface GroupResourceService {

    CommonResult getGroupResources(Long userId, Long groupId, String type, Long classis);

    CommonResult delGroupResource(CmsGroupResource resource);

    CommonResult addFavorite(Long userId, String title, String introduction, String url);

    CommonResult getWeb(Long userId, Long webId);

}