package com.yo.zuulservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Order(2)
@Configuration
public class ApplicationConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    private RsaVerifier verifier;

    @Autowired
    private RsaSigner signer;

    @Autowired
    private MemberDetailsService memberDetailsService;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private JwtHeadFilter jwtHeadFilter;

    @Bean
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler();
    }

    @Autowired
    AccessDecisionService accessDecisionService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //登录过滤器
        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter();
        jwtLoginFilter.setAuthenticationManager(this.authenticationManagerBean());

        //登录成功和失败的操作
        loginSuccessHandler.setSigner(signer);
        jwtLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        jwtLoginFilter.setAuthenticationFailureHandler(new LoginFailureHandler());

        //登录过滤器的授权提供者(就这么叫吧)
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(memberDetailsService);

        //配置需要对应用户权限的restFul url
        accessDecisionService.setPersonalRestUrl("/member-service/member/{id}/icon","/member-service/member/{id}/info","/member-service/member/{id}/avatarUploadUrl",
                "/note/addNote/{id}","/note/getNotes/{id}","/note/updateNote/{id}","/note/ownNote/{id}/*","/note/note/{userId}/*",
                "/group-service/group/{id}/group", "/group-service/group/{id}/createdGroups","/group-service/group/{id}/*/avatarUploadUrl","/group-service/group/{id}/allGroups","/group-service/group/{id}/members/*","/group-service/group/{id}/groupCode","/group-service/group/{id}/group/member",
                "/group-service/group/{id}/info",
                "/resource/{id}/ownResource","/resource/{id}/ownResource/*",
                "/resource/{id}/ownResource/web",
                "/file/blogFilePutUrl/{id}/*","/file/member/{id}/file","/file/member/{id}/existFile",
                "/zuul/member/{id}/file","/zuul/file/member/{id}/file",
                "/log/readNoteLog/{id}","/log/readNoteLog/{id}/*"
        );

        accessDecisionService.setPermitAllUrl("/resource/resClassis/group/*", "/resource/resClassis/group/*/*", "/resource/resClassis/group/*/*/*", "/resource/group/*/*/*",
                "/note/group/*/*", "/note/group/*/*/*",
                "/file/member/file/*","/file/member/fileInfo/*",
                "/file/group/*/file/*", "/file/group/*/existFile","/group/*/file/*","/zuul/file/group/*/file","/group/*/multipartFile",
                "/zuul/file/group/*/file","file/group/*/file",
                "/resource/group/*/resource/web","/resource/group/*/*/*",
                "/group-service/group/*/member","/file/member/multipartFile");


        //JWT校验过滤器
        jwtHeadFilter.setVerifier(verifier);
        http
                //身份验证入口,当需要登录却没登录时调用
                //具体为,当抛出AccessDeniedException异常时且当前是匿名用户时调用
                //匿名用户: 当过滤器链走到匿名过滤器(AnonymousAuthenticationFilter)时,
                //会进行判断SecurityContext是否有凭证(Authentication),若前面的过滤器都没有提供凭证,
                //匿名过滤器会给SecurityContext提供一个匿名的凭证(可以理解为用户名和权限为anonymous的Authentication),
                //这也是JwtHeadFilter发现请求头中没有jwtToken不作处理而直接进入下一个过滤器的原因
                .exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("需要登陆");
        })

                //拒绝访问处理,当已登录,但权限不足时调用
                //抛出AccessDeniedException异常时且当不是匿名用户时调用
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("没有权限");
                })
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/member-service/member/*/info/*","/member-service/member/*/icon").permitAll()
                .antMatchers("/file/presignedUrl","/file/uploadUrl/*","/file/uploadResource","/file/iconUploadUrl","/file/groupAvatarUploadUrl",
                        "/member-service/member/area/*","/member-service/member/register","/member-service/member/info/exist","/member-service/member/mailVerificationCode","/member-service/member/*/info",
                        "/note/getNote/*/*","/note/updateNote/*","note/search",
                        "/group-service/group/*/group","/group-service/group/*/createdGroups",
                        "/group-service/group/*/*/byAdmin",
                        "/login.html","/file/group/**","/zull/file/group/*/file",
                        "/group-service/group/*/member",
                        "/log/readNoteLogDetails/*").permitAll()
                .anyRequest().access("@accessDecisionService.hasPermission(request , authentication)")
                .and()
                //将授权提供者注册到授权管理器中(AuthenticationManager)
                .authenticationProvider(provider)
                .addFilterAfter(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtHeadFilter, JwtLoginFilter.class)
                //禁用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
