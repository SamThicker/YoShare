package com.yo.resourceservice.service;

import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.model.CmsGroupResource;

public interface GroupResourceService {

    CommonResult getGroupResources(Long userId, Long groupId, String type, Long classis);

    CommonResult delGroupResource(CmsGroupResource resource, String groupId) throws Exception;

    CommonResult addFavorite(Long userId, String title, String introduction, String url, String classis);

    CommonResult getWeb(Long userId, Long webId);

}