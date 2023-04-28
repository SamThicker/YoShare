package com.yo.zuulservice.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yo.yoshare.common.api.CommonResult;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 设置过滤路径,即设置登录的请求路径
     * */
    JwtLoginFilter() {
        super(new AntPathRequestMatcher("/security/member/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        try {
            //从Json中获取username和password
            String body = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
            String account = null, password = null;
            if (StringUtils.hasText(body)) {
                JSONObject jsonObj = JSON.parseObject(body);
                account = jsonObj.getString("account");
                password = jsonObj.getString("password");
            }
            account = account == null ? "" : account;
            password = password == null ? "" : password;
            account = account.trim();
            //创建待认证的Token,此时凭证中的主体principal为用户名
            JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(account, password);
            //将认证详情(ip,sessionId)写到凭证
            jwtAuthenticationToken.setDetails(new WebAuthenticationDetails(request));
            //生成已认证的凭证,此时凭证中的主体为userDetails
            Authentication authenticatedToken = this.getAuthenticationManager().authenticate(jwtAuthenticationToken);
            return authenticatedToken;
        }catch (Exception e){
            e.printStackTrace();
            if (e instanceof AuthenticationException){
                throw e;
            }
            throw new RuntimeException("服务异常");
        }
    }
}
