package com.yo.zuulservice.controller;

import com.yo.yoshare.common.api.CommonResult;
import com.yo.zuulservice.feignInterface.MemberFeign;
import com.yo.zuulservice.feignInterface.SecurityFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ZuulServiceController {

    @Autowired
    MemberFeign memberFeign;

    @RequestMapping("/gateway/security/info")
    public CommonResult getMemberInfo(HttpServletRequest request, HttpServletResponse response){
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        String username;
        if (auth.getPrincipal() instanceof UserDetails) {
            UserDetails user = (UserDetails) auth.getPrincipal();
            username = user.getUsername();
        }else {
            username = (String)auth.getPrincipal();
        }
        return memberFeign.getMemberInfo(username,"account");
    }

}
