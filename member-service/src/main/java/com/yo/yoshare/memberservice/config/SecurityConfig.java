package com.yo.yoshare.memberservice.config;


import com.yo.yoshare.memberservice.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;/*
import org.springframework.security.config.annotation.method.config.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.config.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;*/
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
/*@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)*/
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}




