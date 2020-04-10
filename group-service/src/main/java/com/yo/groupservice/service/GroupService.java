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

    CommonResult createGroup(String id, GmsGroup group) throws GlobalException.GroupTooMuchException;

    CommonResult getGroupsByUserId(String id);

    CommonResult getAvatarUploadUrl(String id, String groupId, String type) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidPortException, InvalidExpiresRangeException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, ErrorResponseException;

    List<GmsGroup> getMemberGroups(Long id);

    CommonResult listAllGroupMember(Long userId, Long groupId);

    CommonResult getGroupJoinCode(Long userId, Long groupId);

    CommonResult joinGroupByCode(Long userId, Long groupId, String groupJoinCode);

    CommonResult updateGroupInfo(Long userId, GmsGroup group);
}
