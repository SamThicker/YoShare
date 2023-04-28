package com.yo.zuulservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Order(1)
@Configuration
public class Test {

    @Bean
    JwtHeadFilter jwtHeadFilter(){
        return new JwtHeadFilter();
    }
}
