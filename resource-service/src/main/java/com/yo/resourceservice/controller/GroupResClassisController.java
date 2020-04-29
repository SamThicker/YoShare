package com.yo.resourceservice.controller;

import com.yo.resourceservice.bo.ResourceType;
import com.yo.resourceservice.service.GroupResClassisService;
import com.yo.yoshare.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/resource/resClassis/group")
public class GroupResClassisController {

    @Autowired
    private GroupResClassisService classisService;
    @Autowired
    private HttpServletRequest request;

    @PostMapping(value = "{groupId}/{type}")
    public CommonResult addGroupResClassis(@PathVariable(value = "groupId") Long groupId,
                                           @RequestParam("name") String name,
                                           @PathVariable("type") String type){
        Long userId = Long.valueOf(request.getHeader("userId"));
        if (!ResourceType.isContain(type)) {
            return CommonResult.failed("类型不正确");
        }
        return classisService.addClassis(userId, groupId, name, type);
    }

    @GetMapping(value = "{groupId}/{type}")
    public CommonResult getGroupResClassisByType(@PathVariable(value = "groupId") Long groupId,
                                           @PathVariable("type") String type){
        Long userId = Long.valueOf(request.getHeader("userId"));
        if (!ResourceType.isContain(type)) {
            return CommonResult.failed("类型不正确");
        }
        return classisService.getClassisByType(userId, groupId, type);
    }

    @PostMapping(value = "{groupId}/{type}/{classisId}")
    public CommonResult updateGroupResClassis(@PathVariable(value = "groupId") Long groupId,
                                           @PathVariable("type") String type,
                                           @PathVariable("classisId") Long classisId,
                                           @RequestParam("name") String name){
        Long userId = Long.valueOf(request.getHeader("userId"));
        if (!ResourceType.isContain(type)) {
            return CommonResult.failed("类型不正确");
        }
        return classisService.updateClassis(userId, groupId, type, classisId, name);
    }

    @DeleteMapping(value = "{groupId}/{type}/{classisId}")
    public CommonResult deleteGroupResClassis(@PathVariable(value = "groupId") Long groupId,
                                           @PathVariable("type") String type,
                                           @PathVariable("classisId") Long classisId){
        Long userId = Long.valueOf(request.getHeader("userId"));
        if (!ResourceType.isContain(type)) {
            return CommonResult.failed("类型不正确");
        }
        return classisService.deleteClassis(userId, groupId, type, classisId);
    }


}
