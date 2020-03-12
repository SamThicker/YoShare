package com.yo.zuulservice.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.mapper.UmsMemberSecurityMapper;
import com.yo.yoshare.mbg.model.UmsMemberSecurity;
import com.yo.yoshare.mbg.model.UmsMemberSecurityExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler/*SimpleUrlAuthenticationSuccessHandler*/ {

    private RsaSigner signer;

    @Autowired
    UmsMemberSecurityMapper umsMemberSecurityMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UmsMemberSecurityExample example = new UmsMemberSecurityExample();
        String username = authentication.getName();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsMemberSecurity> list = umsMemberSecurityMapper.selectByExample(example);
        UmsMemberSecurity info = list.size()>0 ? list.get(0) : null;
        response.setContentType("application/json;charset=UTF-8");
        String userJsonStr = JSON.toJSONString(new JwtTokenPayloadTemplate(info.getId()));
        String token = JwtHelper.encode(userJsonStr, signer).getEncoded();
        String objectStr="{\"token\":\""+token+"\"}";
        JSONObject jsonObject = JSONObject.parseObject(objectStr);
        //签发token
        response.getWriter().write(JSON.toJSONString(CommonResult.success(jsonObject,"登录成功")));
    }

    public void setSigner(RsaSigner signer) {
        this.signer = signer;
    }
}
