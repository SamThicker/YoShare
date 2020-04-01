package com.yo.yoshare.memberservice.service.impl;

import com.yo.yoshare.common.area.Area;
import com.yo.yoshare.mbg.mapper.AreaDataMapper;
import com.yo.yoshare.mbg.model.AreaData;
import com.yo.yoshare.mbg.model.AreaDataExample;
import com.yo.yoshare.memberservice.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    AreaDataMapper areaDataMapper;

    static int IS_PROVINCE_FLAG = 99;
    static int IS_REGION_FLAG = 9999;

    @Override
    public List getAreaByCode(int code) {
        AreaDataExample example = new AreaDataExample();
        example.createCriteria().andParentidEqualTo(code);
        List<AreaData> areaList = areaDataMapper.selectByExample(example);
        if (0 == code){
            return areaList.stream().map(Area::toProvince).collect(Collectors.toList());
        } else if (IS_PROVINCE_FLAG >= code){
            return areaList.stream().map(Area::toCity).collect(Collectors.toList());
        } else if (IS_REGION_FLAG >= code){
            return areaList.stream().map(Area::toRegion).collect(Collectors.toList());
        } else {
            example.clear();
            example.createCriteria().andCodeidEqualTo(code);
            return areaDataMapper.selectByExample(example).stream().map(Area::toRegion).collect(Collectors.toList());
        }
    }
}
