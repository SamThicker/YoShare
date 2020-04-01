package com.yo.groupservice.service.groupServiceImpl;

import com.yo.groupservice.dao.GmsGroupDao;
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
    @Autowired
    private FileService fileService;
    @Autowired(required = false)
    private GmsGroupDao groupDao;

    @Override
    public CommonResult createGroup(String id, GmsGroup group) throws GlobalException.GroupTooMuchException {
        GmsGroupExample example = new GmsGroupExample();
        example.createCriteria().andCreatedByEqualTo(id);
        List list = groupMapper.selectByExample(example);
        if (list == null || list.size() >= MAX_CREATE_NUM){
           throw new GlobalException.GroupTooMuchException("每个用户只能创建3个小组");
        }
        group.setMemberNum(1);
        group.setCreatedTime(new Date());
        group.setCreatedBy(id);
        //插入记录并返回id
        groupMapper.insertSelective(group);
        Long groupId = group.getId();
        //添加用户与小组的关联记录
        GmsGroupMemberRelationship relationship = new GmsGroupMemberRelationship();
        relationship.setGroupId(groupId);
        relationship.setJoinedTime(new Date());
        relationship.setMemberId(Long.parseLong(id));
        relationship.setUpdatedTime(new Date());
        memberRelationshipMapper.insertSelective(relationship);
        return CommonResult.success(group, "操作成功");
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
        GmsGroup group = new GmsGroup();
        group.setId(Long.parseLong(groupId));
        group.setIcon("/static/groupAvatar/" + fileName);
        groupMapper.updateByPrimaryKeySelective(group);
        return fileService.getGroupAvatarUploadUrl("groupAvatar/" + fileName);
    }

    @Override
    public List<GmsGroup> getMemberGroups(Long id) {
        return groupDao.selectMemberGroup(id);
    }

    @Override
    public CommonResult listAllGroupMember(Long userId, Long groupId) {
        GmsGroupMemberRelationshipExample example = new GmsGroupMemberRelationshipExample();
        example.createCriteria().andMemberIdEqualTo(userId).andGroupIdEqualTo(groupId);
        List result = memberRelationshipMapper.selectByExample(example);
        if (null == result || result.size() <= 0){
            return CommonResult.forbidden("无权限");
        }
        List<UmsMemberInfo> resultList = groupDao.listGroupMember(groupId);
        return CommonResult.success(resultList, "操作成功");
    }
}
