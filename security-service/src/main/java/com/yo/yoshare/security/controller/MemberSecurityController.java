package com.yo.yoshare.security.controller;

import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.security.service.SecurityService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "SecurityController", description = "安全")
@RestController
public class MemberSecurityController {

    @Autowired
    SecurityService securityService;

    @RequestMapping(value = "/member/login", method = RequestMethod.POST)
    public CommonResult<String> login(@RequestParam("username") String username,
                                      @RequestParam("password") String password) {
        String token = securityService.login(username, password);
        return CommonResult.success(token);
    }


}
