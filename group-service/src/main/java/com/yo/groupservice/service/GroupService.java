package com.yo.groupservice.service;

import com.yo.groupservice.exception.GlobalException;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.model.GmsGroup;
import io.minio.errors.*;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface GroupService {

    /**创建小组*/
    CommonResult createGroup(String id, GmsGroup group) throws GlobalException.GroupTooMuchException;

    /**获取自己创建的小组*/
    CommonResult getOwnGroupsByUserId(String id);

    /**获取更换小组头像的URL*/
    CommonResult getAvatarUploadUrl(String id, String groupId, String type) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidPortException, InvalidExpiresRangeException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, ErrorResponseException;

    /**获取用户加入的小组*/
    List<GmsGroup> getMemberGroups(Long id);

    /**列出小组成员*/
    CommonResult listAllGroupMember(Long userId, Long groupId);

    /**获取加入小组的验证码*/
    CommonResult getGroupJoinCode(Long userId, Long groupId);

    /**通过验证码加入小组*/
    CommonResult joinGroupByCode(Long userId, Long groupId, String groupJoinCode);

    /**更新小组信息*/
    CommonResult updateGroupInfo(Long userId, GmsGroup group);

    /**判断用户是否小组管理员
     * @return*/
    boolean isGroupAdmin(Long groupId, Long userId);

    CommonResult quitGroup(Long groupId);
}
