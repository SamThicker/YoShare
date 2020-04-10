package com.yo.resourceservice.service.serviceImpl;

import com.yo.resourceservice.bo.ResourceType;
import com.yo.resourceservice.feignInterface.NoteService;
import com.yo.resourceservice.service.ResourceService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.common.api.ResultCode;
import com.yo.yoshare.mbg.mapper.CmsGroupResourceClassificationMapper;
import com.yo.yoshare.mbg.mapper.CmsMemberResourceClassficationMapper;
import com.yo.yoshare.mbg.mapper.CmsMemberResourceMapper;
import com.yo.yoshare.mbg.model.CmsMemberResource;
import com.yo.yoshare.mbg.model.CmsMemberResourceClassfication;
import com.yo.yoshare.mbg.model.CmsMemberResourceClassficationExample;
import com.yo.yoshare.mbg.model.CmsMemberResourceExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired(required = false)
    CmsMemberResourceMapper memberResourceMapper;
    @Autowired(required = false)
    CmsMemberResourceClassficationMapper memberClassificationMapper;
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

    @Override
    public CommonResult addClassification(Long userId, String type, String classificationName) {
        if (!ResourceType.isContain(type)){
            return CommonResult.failed("资源类型错误");
        }
        CmsMemberResourceClassficationExample example = new CmsMemberResourceClassficationExample();
        example.createCriteria().andMemberIdEqualTo(userId).andClassficationNameEqualTo(classificationName).andTypeEqualTo(type);
        List list = memberClassificationMapper.selectByExample(example);
        if (list.size()>0){
            return CommonResult.failed("已经存在该分类，请更换名称");
        }
        example.clear();
        example.createCriteria().andMemberIdEqualTo(userId).andTypeEqualTo(type);
        List all = memberClassificationMapper.selectByExample(example);
        if (all.size()>=20){
            return CommonResult.failed("仅允许创建20个自定分类");
        }
        CmsMemberResourceClassfication classfication = new CmsMemberResourceClassfication();
        classfication.setMemberId(userId);
        classfication.setClassficationName(classificationName);
        classfication.setType(type);
        memberClassificationMapper.insertSelective(classfication);
        return CommonResult.success("操作成功");
    }

    @Override
    public CommonResult getClassification(Long userId, String type) {
        CmsMemberResourceClassficationExample example = new CmsMemberResourceClassficationExample();
        example.createCriteria().andMemberIdEqualTo(userId);
        List result = memberClassificationMapper.selectByExample(example);
        return CommonResult.success(result);
    }

    @Override
    public CommonResult updateClassification(Long userId, CmsMemberResourceClassfication memberResourceClassfication) {
        CmsMemberResourceClassficationExample example = new CmsMemberResourceClassficationExample();
        example.createCriteria().andMemberIdEqualTo(userId).andIdEqualTo(memberResourceClassfication.getId());
        memberClassificationMapper.updateByExampleSelective(memberResourceClassfication, example);
        return CommonResult.success("成功");
    }

    @Override
    public CommonResult deleteClassification(Long userId, Long classificationId) {
        CmsMemberResourceClassficationExample example = new CmsMemberResourceClassficationExample();
        example.createCriteria().andIdEqualTo(classificationId).andMemberIdEqualTo(userId);
        memberClassificationMapper.deleteByExample(example);
        return CommonResult.success("成功");
    }

}
