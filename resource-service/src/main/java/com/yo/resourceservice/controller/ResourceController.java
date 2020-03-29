package com.yo.resourceservice.controller;

import com.yo.resourceservice.service.ResourceService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.model.CmsMemberResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ResourceController {

    @Autowired
    ResourceService resourceService;

    @GetMapping("/{id}/ownResource")
    public CommonResult getResourcesForSelf(@PathVariable("id") Long id) {
        List<CmsMemberResource> list = resourceService.getResourcesForSelf(id);
        return CommonResult.success(list, "操作成功");
    }
}
