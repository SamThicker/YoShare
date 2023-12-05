package com.yo.zuulservice.feignInterface;

import com.yo.yoshare.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@FeignClient(value = "security-service")
public interface SecurityFeign {


    @RequestMapping(value = "/security/member/login", method = RequestMethod.POST)
    CommonResult<String> login(@RequestParam("username") String username,
                               @RequestParam("password") String password);
}
