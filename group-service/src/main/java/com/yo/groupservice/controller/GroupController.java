package com.yo.groupservice.controller;

import com.yo.groupservice.exception.GlobalException;
import com.yo.groupservice.service.GroupService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.model.GmsGroup;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

    @GetMapping(value = "/{id}/{groupId}/avatarUploadUrl")
    public CommonResult getAvatarUploadUrl(@PathVariable(value = "id") String id,
                                           @PathVariable(value = "groupId") String groupId,
                                           String type) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, InvalidExpiresRangeException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, ErrorResponseException {
        return groupService.getAvatarUploadUrl(id, groupId, type);
    }

    @GetMapping(value = "/{id}/allGroups")
    public CommonResult getMemberAllGroup(@PathVariable(value = "id") Long id){
        return CommonResult.success(groupService.getMemberGroups(id),"操作成功");
    }


}
