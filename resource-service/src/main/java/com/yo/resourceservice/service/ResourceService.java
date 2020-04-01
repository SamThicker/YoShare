package com.yo.resourceservice.service;

import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.model.CmsMemberResource;

import java.util.List;

public interface ResourceService {
    List<CmsMemberResource> getResourcesForSelf(Long id);

    CommonResult delResourceForSelf(CmsMemberResource resource);
}
