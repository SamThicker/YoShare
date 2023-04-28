package com.yo.resourceservice.service;

import com.yo.yoshare.common.api.CommonResult;

public interface GroupResClassisService {
    CommonResult addClassis(Long userId, Long groupId, String name, String type);

    CommonResult getClassisByType(Long userId, Long groupId, String type);

    CommonResult updateClassis(Long userId, Long groupId, String type, Long classisId, String name);

    CommonResult deleteClassis(Long userId, Long groupId, String type, Long classisId);
}
