package com.yo.zuulservice.feignInterface;

import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.memberservice.dto.UmsMemberSimpleInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "service-member")
public interface MemberFeign {

    @RequestMapping(value = "/{param}/info/{option}", method = RequestMethod.GET)
    CommonResult<UmsMemberSimpleInfo> getMemberInfo(@PathVariable("param") String param,
                                                           @PathVariable("option") String option);
}



