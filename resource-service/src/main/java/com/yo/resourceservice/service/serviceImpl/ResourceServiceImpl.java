package com.yo.resourceservice.service.serviceImpl;

import com.yo.resourceservice.service.ResourceService;
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


    @Override
    public List<CmsMemberResource> getResourcesForSelf(Long id) {
        CmsMemberResourceExample example = new CmsMemberResourceExample();
        example.createCriteria().andByUserIdEqualTo(id);
        List<CmsMemberResource> list = memberResourceMapper.selectByExample(example);
        return list;
    }
}
