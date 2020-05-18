package com.yo.groupservice.service.groupServiceImpl;

import com.yo.groupservice.dao.GmsGroupDao;
import com.yo.groupservice.exception.GlobalException;
import com.yo.groupservice.feign.FileService;
import com.yo.groupservice.service.GroupService;
import com.yo.groupservice.service.RedisService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.mapper.GmsGroupMapper;
import com.yo.yoshare.mbg.mapper.GmsGroupMemberRelationshipMapper;
import com.yo.yoshare.mbg.model.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    /**加入小组的验证码*/
    @Value("${redis.key.prefix.groupJoinCode}")
    private String GROUP_JOIN_CODE_PREFIX;
    /**加入小组的验证码的超时时间*/
    @Value("${redis.key.expire.groupJoinCode}")
    private Long GROUP_JOIN_CODE_EXPIRE;
    /**小组人数限制*/
    @Value("${group.member.limit}")
    private int GROUP_MEMBER_LIMIT;
    @Autowired
    private RedisService redisService;

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
        relationship.setStatus("1");
        memberRelationshipMapper.insertSelective(relationship);
        return CommonResult.success(group, "操作成功");
    }

    @Override
    public CommonResult getOwnGroupsByUserId(String id) {
        GmsGroupExample example = new GmsGroupExample();
        example.createCriteria().andCreatedByEqualTo(id);
        List list = groupMapper.selectByExample(example);
        return CommonResult.success(list, "操作成功");
    }

    @Override
    public CommonResult getAvatarUploadUrl(String id, String groupId, String type) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidPortException, InvalidExpiresRangeException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, ErrorResponseException {
        if (!isMember(Long.valueOf(id), Long.valueOf(groupId))) {
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
        if(!isMember(userId, groupId)) {
            return CommonResult.forbidden("无权限");
        }
        List<UmsMemberInfo> resultList = groupDao.listGroupMember(groupId);
        return CommonResult.success(resultList, "操作成功");
    }

    @Override
    public CommonResult getGroupJoinCode(Long userId, Long groupId) {
        if (!isMember(userId, groupId)) {
            return CommonResult.failed("无权限");
        }
        GmsGroupMemberRelationshipExample example = new GmsGroupMemberRelationshipExample();
        example.clear();
        example.createCriteria().andMemberIdEqualTo(groupId);
        int num = memberRelationshipMapper.countByExample(example);
        if (num >= GROUP_MEMBER_LIMIT){
            return CommonResult.failed("人数已满");
        }
        String code = UUID.randomUUID().toString();
        String key = GROUP_JOIN_CODE_PREFIX + groupId + "";
        redisService.set(key, code);
        redisService.expire(key,GROUP_JOIN_CODE_EXPIRE);
        return CommonResult.success(code, "操作成功");
    }

    @Override
    public CommonResult joinGroupByCode(Long userId, Long groupId, String groupJoinCode) {
        if(isMember(userId, groupId)) {
            return CommonResult.failed("您已经在小组中");
        }
        groupJoinCode = groupJoinCode.trim();
        String key = GROUP_JOIN_CODE_PREFIX + groupId;
        String code = redisService.get(key);
        if (code == null) {
            return CommonResult.failed("请联系该小组，重新分发验证码");
        }
        if (!code.equals(groupJoinCode)) {
            return CommonResult.failed("验证码错误");
        }
        redisService.remove(key);
        GmsGroup group = groupMapper.selectByPrimaryKey(groupId);
        if (group.getMemberNum() >= 5){
            return CommonResult.failed("人数已满");
        }
        group.setUpdatedTime(new Date());
        group.setUpdatedBy(userId.toString());
        group.setMemberNum(group.getMemberNum()+1);
        groupMapper.updateByPrimaryKeySelective(group);
        GmsGroupMemberRelationship relationship = new GmsGroupMemberRelationship();
        relationship.setGroupId(groupId);
        relationship.setJoinedTime(new Date());
        relationship.setMemberId(userId);
        relationship.setStatus("1");
        memberRelationshipMapper.insertSelective(relationship);
        return CommonResult.success("操作成功", "操作成功");
    }

    @Override
    public CommonResult updateGroupInfo(Long userId, GmsGroup group) {
        if (!isMember(userId, group.getId())){
            return CommonResult.failed("无权限");
        }
        GmsGroup newGroup = new GmsGroup();
        newGroup.setId(group.getId());
        newGroup.setName(group.getName());
        newGroup.setIntroduction(group.getIntroduction());
        newGroup.setUpdatedBy(userId.toString());
        newGroup.setUpdatedTime(new Date());
        groupMapper.updateByPrimaryKeySelective(newGroup);
        return CommonResult.success(groupMapper.selectByPrimaryKey(group.getId()),"操作成功");
    }


    @Override
    public CommonResult isGroupAdmin(Long groupId, Long userId) {
        return null;
    }


    /**判断用户是否小组成员*/
    public boolean isMember(Long userId, Long groupId){
        GmsGroupMemberRelationshipExample example = new GmsGroupMemberRelationshipExample();
        example.createCriteria().andMemberIdEqualTo(userId).andGroupIdEqualTo(groupId).andStatusEqualTo("1");
        return 0 < memberRelationshipMapper.countByExample(example);
    }
}
