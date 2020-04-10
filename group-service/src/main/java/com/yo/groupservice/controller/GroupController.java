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

    /**创建小组*/
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

    @PatchMapping(value = "/{id}/info")
    public CommonResult updateGroupInfo(@PathVariable(value = "id") Long userId,
                                        @RequestBody GmsGroup group){
        return groupService.updateGroupInfo(userId, group);

    }

    /**获取用户创建的小组*/
    @GetMapping(value = "/{id}/createdGroups")
    public CommonResult getGroupsByUserId(@PathVariable String id) {
        return groupService.getGroupsByUserId(id);
    }

    /**获取上传头像的url*/
    @GetMapping(value = "/{id}/{groupId}/avatarUploadUrl")
    public CommonResult getAvatarUploadUrl(@PathVariable(value = "id") String id,
                                           @PathVariable(value = "groupId") String groupId,
                                           String type) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, InvalidExpiresRangeException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, ErrorResponseException {
        return groupService.getAvatarUploadUrl(id, groupId, type);
    }

    /**获取用户所有的小组*/
    @GetMapping(value = "/{id}/allGroups")
    public CommonResult getMemberAllGroup(@PathVariable(value = "id") Long id){
        return CommonResult.success(groupService.getMemberGroups(id),"操作成功");
    }

    /**获取加入小组的验证码*/
    @GetMapping(value = "/{id}/groupCode")
    public CommonResult getGroupJoinCode(@PathVariable(value = "id") Long userId,
                                           @RequestParam Long groupId){
        return groupService.getGroupJoinCode(userId, groupId);
    }

    /**通过激活码加入小组*/
    @PutMapping(value = "/{id}/group/member")
    public CommonResult joinGroupByCode(@PathVariable(value = "id") Long userId,
                                         @RequestParam(value = "groupJoinCode") String groupJoinCode,
                                        @RequestParam(value = "groupId") Long groupId){
        return groupService.joinGroupByCode(userId, groupId, groupJoinCode);
    }

    /**获取小组的所有成员信息*/
    @GetMapping(value = "/{id}/group/member")
    public CommonResult getGroupMembers(@PathVariable(value = "id") Long userId,
                                        @RequestParam(value = "groupId") Long groupId){
        return groupService.listAllGroupMember(userId,groupId);
    }


}
