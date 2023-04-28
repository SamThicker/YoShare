package com.yo.resourceservice.controller;

import com.yo.resourceservice.service.GroupResourceService;
import com.yo.resourceservice.service.ResourceService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.model.CmsGroupResource;
import com.yo.yoshare.mbg.model.CmsMemberResource;
import com.yo.yoshare.mbg.model.CmsMemberResourceClassfication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("resource/group")
public class GroupResourceController {

    @Autowired
    GroupResourceService groupResourceService;
    @Autowired
    HttpServletRequest request;

    @GetMapping("/{groupId}/{type}/{classis}")
    public CommonResult getGroupResources(@PathVariable(value = "groupId") Long groupId,
            @PathVariable String type, @PathVariable("classis") Long classis) {
        Long userId = Long.valueOf(request.getHeader("userId"));
        return groupResourceService.getGroupResources(userId, groupId, type, classis);
    }

    @DeleteMapping("/{groupId}/{type}/{classis}")
    public CommonResult delResourceForSelf(@PathVariable("groupId") Long groupId, @RequestBody CmsGroupResource resource) throws Exception {
        Long userId = Long.valueOf(request.getHeader("userId"));
        if ( !userId.equals(resource.getByUserId()) ){
            return CommonResult.forbidden(null);
        }
        return groupResourceService.delGroupResource(resource, String.valueOf(groupId));
    }

    /**创建收藏网页*/
    @PutMapping(value = "/{groupId}/resource/web")
    public CommonResult addFavorite(@PathVariable("groupId") Long groupId, @RequestParam String title,
                                    @RequestParam String introduction, @RequestParam String url, @RequestParam String classis){
        return groupResourceService.addFavorite(groupId, title, introduction, url, classis);
    }

    /**获取收藏的网页*/
    @GetMapping(value = "/{groupId}/resource/web")
    public CommonResult getWeb(@PathVariable(value = "groupId") Long groupId, @RequestParam Long webId){
        return groupResourceService.getWeb(groupId, webId);
    }

}
