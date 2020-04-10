package com.yo.resourceservice.controller;

import com.yo.resourceservice.service.ResourceService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.model.CmsMemberResource;
import com.yo.yoshare.mbg.model.CmsMemberResourceClassfication;
import com.yo.yoshare.mbg.model.CmsMemberResourceClassfication;
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

    /**添加新分类*/
    @PutMapping(value = "/{id}/ownResource/classification")
    public CommonResult addClassification(@PathVariable("id") Long userId, @RequestParam String type,
                                          @RequestParam String name){
        return resourceService.addClassification(userId, type, name);
    }

    /**获取用户自定义的分类*/
    @GetMapping(value = "/{id}/ownResource/classification")
    public CommonResult getClassification(@PathVariable("id") Long userId, @RequestParam String type){
        return resourceService.getClassification(userId, type);
    }


    /**更改用户自定义的分类*/
    @PatchMapping(value = "/{id}/ownResource/classification")
    public CommonResult updateClassification(@PathVariable("id") Long userId, @RequestBody CmsMemberResourceClassfication memberResourceClassfication){
        return resourceService.updateClassification(userId, memberResourceClassfication);
    }

    /**删除用户自定义的分类*/
    @DeleteMapping(value = "/{id}/ownResource/classification")
    public CommonResult deleteClassification(@PathVariable("id") Long userId, @RequestParam Long classificationId){
        return resourceService.deleteClassification(userId, classificationId);
    }

}
