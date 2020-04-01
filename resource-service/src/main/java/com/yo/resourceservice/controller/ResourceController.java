package com.yo.resourceservice.controller;

import com.yo.resourceservice.service.ResourceService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.model.CmsMemberResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{id}/ownResource")
    public CommonResult delResourceForSelf(@PathVariable("id") Long id, @RequestBody CmsMemberResource resource) {
        if (!resource.getByUserId().equals(id)){
            return CommonResult.forbidden(null);
        }
        return resourceService.delResourceForSelf(resource);
    }
}
