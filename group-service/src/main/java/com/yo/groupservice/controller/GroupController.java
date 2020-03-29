package com.yo.groupservice.controller;

import com.yo.groupservice.exception.GlobalException;
import com.yo.groupservice.service.GroupService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.model.GmsGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PutMapping(value = "/{id}/group")
    public CommonResult createGroup(@RequestBody GmsGroup group, @PathVariable String id){
        group.setCreatedBy(id);
        try {
            return groupService.createGroup(id, group);
        } catch (GlobalException.GroupTooMuchException e) {
            e.printStackTrace();
            return CommonResult.failed("出错辣");
        }
    }

    @GetMapping(value = "/{id}/createdGroups")
    public CommonResult getGroupsByUserId(@PathVariable String id) {
        return groupService.getGroupsByUserId(id);
    }
}
