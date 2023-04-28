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
@RequestMapping("resource")
public class ResourceController {

    @Autowired
    ResourceService resourceService;

    /**获取用户资源*/
    @GetMapping("/{id}/ownResource")
    public CommonResult getResourcesForSelf(@RequestParam String classification,
            @RequestParam String type, @PathVariable("id") Long id) {
        List<CmsMemberResource> list = resourceService.getResourcesForSelf(id, type, classification);
        return CommonResult.success(list, "操作成功");
    }

    @DeleteMapping("/{id}/ownResource")
    public CommonResult delResourceForSelf(@PathVariable("id") Long id, @RequestBody CmsMemberResource resource) throws Exception {
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

    /**创建收藏网页*/
    @PutMapping(value = "/{id}/ownResource/web")
    public CommonResult addFavorite(@PathVariable("id") Long userId, @RequestParam String title,
                                    @RequestParam String introduction, @RequestParam String url, @RequestParam String classis){
        return resourceService.addFavorite(userId, title, introduction, url, classis);
    }

    /**获取收藏的网页*/
    @GetMapping(value = "/{id}/ownResource/web")
    public CommonResult getWeb(@PathVariable(value = "id") Long userId, @RequestParam Long webId){
        return resourceService.getWeb(userId, webId);
    }

}
