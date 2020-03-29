package com.yo.groupservice.service.groupServiceImpl;

import com.yo.groupservice.exception.GlobalException;
import com.yo.groupservice.service.GroupService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.mapper.GmsGroupMapper;
import com.yo.yoshare.mbg.model.GmsGroup;
import com.yo.yoshare.mbg.model.GmsGroupExample;
import com.yo.yoshare.mbg.model.UmsMemberInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired(required = false)
    private GmsGroupMapper groupMapper;


    @Override
    public CommonResult createGroup(String id, GmsGroup group) throws GlobalException.GroupTooMuchException {
        GmsGroupExample example = new GmsGroupExample();
        example.createCriteria().andCreatedByEqualTo(id);
        List list = groupMapper.selectByExample(example);
        if (list == null || list.size() >= 3){
           throw new GlobalException.GroupTooMuchException("每个用户只能创建3个小组");
        }
        GmsGroup result = null;
        group.setMemberNum(1);
        group.setCreatedTime(new Date());
        group.setCreatedBy(id);
        if (groupMapper.insertSelective(group) == 1) {
            result = groupMapper.selectByPrimaryKey(group.getId());
        }
        return CommonResult.success(result, "操作成功");
    }

    @Override
    public CommonResult getGroupsByUserId(String id) {
        GmsGroupExample example = new GmsGroupExample();
        example.createCriteria().andCreatedByEqualTo(id);
        List list = groupMapper.selectByExample(example);
        return CommonResult.success(list, "操作成功");
    }
}
