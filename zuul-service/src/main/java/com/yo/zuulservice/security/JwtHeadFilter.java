package com.yo.zuulservice.security;

import com.alibaba.fastjson.JSON;
import com.yo.yoshare.mbg.model.UmsMemberSecurity;
import com.yo.yoshare.mbg.model.UmsMemberSecurityExample;
import com.yo.zuulservice.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**拦截请求进行token验证
 * @author niXueChao
 * @date 2019/4/3 15:03.
 */
public class JwtHeadFilter extends OncePerRequestFilter {
    private RsaVerifier verifier;

    @Autowired
    MemberDetailsService memberDetailsService;
    @Autowired
    SecurityService securityService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authentication");
        if (token==null || token.isEmpty()){
            filterChain.doFilter(request,response);
            return;
        }
        Long memberid = null;
        JwtTokenPayloadTemplate payload;
        UserDetails userDetails;
        try {
            Jwt jwt = JwtHelper.decodeAndVerify(token, verifier);
            String claims = jwt.getClaims();
            payload = JSON.parseObject(claims, JwtTokenPayloadTemplate.class);
            memberid = Long.valueOf(String.valueOf(payload.getSub()));
            userDetails = memberDetailsService.loadUserById(memberid);
            if (userDetails instanceof JwtMemberDetails){
                ((JwtMemberDetails)userDetails).setMemberid(memberid);
            }
            //todo: 可以在这里添加检查用户是否过期,冻结...
        }catch (Exception e){
            //这里也可以filterChain.doFilter(request,response)然后return,那最后就会调用
            //.exceptionHandling().authenticationEntryPoint,也就是本列中的"需要登陆"
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("token 失效");
            return;
        }
        //不用处理userDetails为空的情况，当user为空，说明token异常，直接抛出NullPointerException退出认证
        //生成认证成功后的AuthenticationToken
        JwtAuthenticationToken jwtLoginToken = new JwtAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        jwtLoginToken.setAuthenticated(true);
        jwtLoginToken.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(jwtLoginToken);
        filterChain.doFilter(request,response);
    }


    public void setVerifier(RsaVerifier verifier) {
        this.verifier = verifier;
    }
}
