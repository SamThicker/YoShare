package com.yo.resourceservice.service;

import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.model.CmsMemberResource;
import com.yo.yoshare.mbg.model.CmsMemberResourceClassfication;

import java.util.List;

public interface ResourceService {
    List<CmsMemberResource> getResourcesForSelf(Long id);

    CommonResult delResourceForSelf(CmsMemberResource resource);

    CommonResult addClassification(Long userId, String type, String classificationName);

    CommonResult getClassification(Long userId, String type);

    CommonResult updateClassification(Long userId, CmsMemberResourceClassfication memberResourceClassfication);

    CommonResult deleteClassification(Long userId, Long classificationId);
}
