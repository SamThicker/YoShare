package com.yo.groupservice.service.groupServiceImpl;

import com.yo.groupservice.exception.GlobalException;
import com.yo.groupservice.feign.FileService;
import com.yo.groupservice.service.GroupService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.mapper.GmsGroupMapper;
import com.yo.yoshare.mbg.mapper.GmsGroupMemberRelationshipMapper;
import com.yo.yoshare.mbg.model.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired(required = false)
    private GmsGroupMapper groupMapper;
    @Autowired(required = false)
    private GmsGroupMemberRelationshipMapper memberRelationshipMapper;
    /**用户创建小组数限制*/
    private final int MAX_CREATE_NUM = 3;
    @Autowired(required = false)
    private FileService fileService;

    @Override
    public CommonResult createGroup(String id, GmsGroup group) throws GlobalException.GroupTooMuchException {
        GmsGroupExample example = new GmsGroupExample();
        example.createCriteria().andCreatedByEqualTo(id);
        List list = groupMapper.selectByExample(example);
        if (list == null || list.size() >= MAX_CREATE_NUM){
           throw new GlobalException.GroupTooMuchException("每个用户只能创建3个小组");
        }
        GmsGroup result;
        group.setMemberNum(1);
        group.setCreatedTime(new Date());
        group.setCreatedBy(id);
        result = groupMapper.selectByPrimaryKey(group.getId());
        Long groupId = group.getId();
        GmsGroupMemberRelationship relationship = new GmsGroupMemberRelationship();
        relationship.setGroupId(groupId);
        relationship.setJoinedTime(new Date());
        relationship.setMemberId(Long.parseLong(id));
        relationship.setUpdatedTime(new Date());
        memberRelationshipMapper.insertSelective(relationship);
        return CommonResult.success(result, "操作成功");
    }

    @Override
    public CommonResult getGroupsByUserId(String id) {
        GmsGroupExample example = new GmsGroupExample();
        example.createCriteria().andCreatedByEqualTo(id);
        List list = groupMapper.selectByExample(example);
        return CommonResult.success(list, "操作成功");
    }

    @Override
    public CommonResult getAvatarUploadUrl(String id, String groupId, String type) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidPortException, InvalidExpiresRangeException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, ErrorResponseException {
        GmsGroupMemberRelationshipExample example = new GmsGroupMemberRelationshipExample();
        example.createCriteria().andGroupIdEqualTo(Long.parseLong(groupId)).andMemberIdEqualTo(Long.parseLong(id));
        List result = memberRelationshipMapper.selectByExample(example);
        if (null == result || result.size()<=0) {
            return CommonResult.forbidden("无权限");
        }
        String fileName = groupId + "_ORIGINAL." + type;
        return fileService.getGroupAvatarUploadUrl("groupAvatar/" + fileName);
    }
}
