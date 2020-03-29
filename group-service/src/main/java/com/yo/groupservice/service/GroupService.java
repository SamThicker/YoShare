package com.yo.groupservice.service;

import com.yo.groupservice.exception.GlobalException;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.mapper.GmsGroupMapper;
import com.yo.yoshare.mbg.model.GmsGroup;
import org.springframework.beans.factory.annotation.Autowired;

public interface GroupService {

    CommonResult createGroup(String id, GmsGroup group) throws GlobalException.GroupTooMuchException;

    CommonResult getGroupsByUserId(String id);
}
