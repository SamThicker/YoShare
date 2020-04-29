package com.yo.resourceservice.service.serviceImpl;

import com.yo.resourceservice.service.GroupResClassisService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.mapper.CmsGroupResourceClassificationMapper;
import com.yo.yoshare.mbg.mapper.GmsGroupMemberRelationshipMapper;
import com.yo.yoshare.mbg.model.CmsGroupResourceClassification;
import com.yo.yoshare.mbg.model.CmsGroupResourceClassificationExample;
import com.yo.yoshare.mbg.model.GmsGroupMemberRelationshipExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupResClassisServiceImpl implements GroupResClassisService {

    @Autowired
    private CmsGroupResourceClassificationMapper classisMapper;
    @Autowired
    private GmsGroupMemberRelationshipMapper relationshipMapper;

    @Override
    public CommonResult addClassis(Long userId, Long groupId, String name, String type) {
        GmsGroupMemberRelationshipExample example = new GmsGroupMemberRelationshipExample();
        example.createCriteria().andGroupIdEqualTo(groupId).andMemberIdEqualTo(userId);
        int count = relationshipMapper.countByExample(example);
        if (count <=0 ) {
            return CommonResult.failed("无权限");
        }
        CmsGroupResourceClassification classis = new CmsGroupResourceClassification();
        classis.setClassificationName(name);
        classis.setGroupId(groupId);
        classis.setType(type);
        classisMapper.insertSelective(classis);
        return CommonResult.success("操作成功");
    }

    @Override
    public CommonResult getClassisByType(Long userId, Long groupId, String type) {
        GmsGroupMemberRelationshipExample example = new GmsGroupMemberRelationshipExample();
        example.createCriteria().andGroupIdEqualTo(groupId).andMemberIdEqualTo(userId);
        int count = relationshipMapper.countByExample(example);
        if (count <=0 ) {
            return CommonResult.failed("无权限");
        }
        CmsGroupResourceClassificationExample classisExample = new CmsGroupResourceClassificationExample();
        classisExample.createCriteria().andGroupIdEqualTo(groupId).andTypeEqualTo(type);
        return CommonResult.success(classisMapper.selectByExample(classisExample), "操作成功");
    }

    @Override
    public CommonResult updateClassis(Long userId, Long groupId, String type, Long classisId, String name) {
        GmsGroupMemberRelationshipExample example = new GmsGroupMemberRelationshipExample();
        example.createCriteria().andGroupIdEqualTo(groupId).andMemberIdEqualTo(userId);
        int count = relationshipMapper.countByExample(example);
        if (count <=0 ) {
            return CommonResult.failed("无权限");
        }
        CmsGroupResourceClassificationExample classisExample = new CmsGroupResourceClassificationExample();
        classisExample.createCriteria().andGroupIdEqualTo(groupId).andTypeEqualTo(type).andIdEqualTo(classisId);
        CmsGroupResourceClassification classis = new CmsGroupResourceClassification();
        classis.setType(type);
        classis.setGroupId(groupId);
        classis.setClassificationName(name);
        return CommonResult.success(classisMapper.updateByExampleSelective(classis, classisExample), "操作成功");
    }

    @Override
    public CommonResult deleteClassis(Long userId, Long groupId, String type, Long classisId) {
        GmsGroupMemberRelationshipExample example = new GmsGroupMemberRelationshipExample();
        example.createCriteria().andGroupIdEqualTo(groupId).andMemberIdEqualTo(userId);
        int count = relationshipMapper.countByExample(example);
        if (count <=0 ) {
            return CommonResult.failed("无权限");
        }
        CmsGroupResourceClassificationExample classisExample = new CmsGroupResourceClassificationExample();
        classisExample.createCriteria().andGroupIdEqualTo(groupId).andTypeEqualTo(type).andIdEqualTo(classisId);
        classisMapper.deleteByExample(classisExample);
        return CommonResult.success("操作成功");
    }
}
