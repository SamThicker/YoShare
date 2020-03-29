package com.yo.zuulservice.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** 配置路径访问限制,若你的用户角色比较简单,不需要存数据库,
 * 可以在ApplicationConfigurerAdapter里配置如
 *    httpSecurity
 *    .authorizeRequests()
 *    .antMatchers("/order").....
 *
 * @author niXueChao
 * @date 2019/4/10 10:33.
 */
@Component("accessDecisionService")
public class AccessDecisionService {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    private List<String> personalRestUrl = new ArrayList<>();

    public boolean hasPermission(HttpServletRequest request, Authentication auth) {
        //Adapter已经过滤了可匿名访问的路径，余下的所有路劲需要进行过滤
        //auth是AnonymousAuthenticationToken子类，说明当前用户未登录，对该请求进行拦截
        if (auth instanceof AnonymousAuthenticationToken) {
            return false;
        }

        JwtMemberDetails user = (JwtMemberDetails) auth.getPrincipal();
        //根据用户名查出能访问哪些url, urls=findUrlByUserName()
        List<String> urls = queryUrlByUserId(user.getMemberid());
        for (String url : urls) {
            if (antPathMatcher.match(url, request.getRequestURI())) {
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * 模拟数据库查询用户权限
     *
     * @param id
     * @return
     */
    private List<String> queryUrlByUserId(Long id) {
        String idstr = String.valueOf(id);
        ArrayList<String> list = new ArrayList<>();
        list.addAll(getRestPermitedUrl(idstr));
        list.add("/gateway/security/info");
        return  list;
    }

    public List<String> getRestPermitedUrl(String id){
        List<String> list = new ArrayList<>();
        for (String temp : this.personalRestUrl){
            list.add( temp.replace("{id}",id) );
        }
        System.out.println(list);
        return list;
    }

    public void setPersonalRestUrl(String...url){
        this.personalRestUrl.addAll(Arrays.asList(url));
    }



}
