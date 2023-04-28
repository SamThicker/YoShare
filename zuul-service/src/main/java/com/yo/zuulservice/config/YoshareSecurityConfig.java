package com.yo.zuulservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import java.util.Arrays;

public class YoshareSecurityConfig extends WebSecurityConfigurerAdapter {

   /* @Autowired
    UserDetailsService userDetailsService;


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        *//*httpSecurity.authorizeRequests()
                .antMatchers("/**","index","error").permitAll()
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/user")
                //自定义参数名，与前端的参数名一致
                .usernameParameter("myusername").passwordParameter("mypassword")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login");*//*
        httpSecurity
                *//*.formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/security/member/login")
                .and()*//*
                .formLogin()
                .loginPage("/login.html")
                //.loginProcessingUrl("/security/member/login")
                .and()
                .authorizeRequests()
                .antMatchers("/login.html", "/security/member/login", "/member/register").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
                .sessionManagement().disable()  //禁用session
                //.formLogin().disable() //禁用form登录
                .cors()  //支持跨域
                .and()   //添加header设置，支持跨域和ajax请求
                .headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
                new Header("Access-control-Allow-Origin","*"))))
                //new Header("Access-Control-Expose-Headers","Authorization"))))
                .and() //拦截OPTIONS请求，直接返回header
                //.addFilterAfter(new OptionRequestFilter(), CorsFilter.class)
                //添加登录filter
                .apply(new JsonLoginConfigurer<>()).loginSuccessHandler(jsonLoginSuccessHandler())
                .and()
                //添加token的filter
                .apply(new JwtLoginConfigurer<>()).permissiveRequestUrls("/logout");

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        auth.authenticationProvider(jwtAuthenticationProvider())
            .authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean("jwtAuthenticationProvider")
    protected AuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider();
    }

    @Bean("daoAuthenticationProvider")
    protected AuthenticationProvider daoAuthenticationProvider() throws Exception{
        //这里会默认使用BCryptPasswordEncoder比对加密后的密码，注意要跟createUser时保持一致
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService());
        return daoProvider;
    }




    @Bean
    protected JsonLoginSuccessHandler jsonLoginSuccessHandler() {
        return new JsonLoginSuccessHandler();
    }


*/
}
