package com.yo.resourceservice.service.serviceImpl;

import com.yo.resourceservice.feignInterface.NoteService;
import com.yo.resourceservice.service.ResourceService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.common.api.ResultCode;
import com.yo.yoshare.mbg.mapper.CmsMemberResourceMapper;
import com.yo.yoshare.mbg.model.CmsMemberResource;
import com.yo.yoshare.mbg.model.CmsMemberResourceExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired(required = false)
    CmsMemberResourceMapper memberResourceMapper;
    @Autowired
    NoteService noteService;

    @Override
    public List<CmsMemberResource> getResourcesForSelf(Long id) {
        CmsMemberResourceExample example = new CmsMemberResourceExample();
        example.createCriteria().andByUserIdEqualTo(id);
        return memberResourceMapper.selectByExample(example);
    }

    @Override
    public CommonResult delResourceForSelf(CmsMemberResource resource) {
        CommonResult result = noteService.delNote(resource.getByUserId().toString(), resource.getResourceRef());
        if (result.getCode()!= ResultCode.SUCCESS.getCode()) {
            return CommonResult.failed("操作失败");
        }
        CmsMemberResourceExample example = new CmsMemberResourceExample();
        example.createCriteria().andByUserIdEqualTo(resource.getByUserId())
                .andTypeEqualTo("NOTE")
                .andIdEqualTo(resource.getId());
        memberResourceMapper.deleteByExample(example);
        return CommonResult.success("操作成功");
    }
}
