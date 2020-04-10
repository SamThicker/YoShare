//package com.yo.yoshare.security.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
//
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManager() throws Exception{
//        AuthenticationManager manager = super.authenticationManagerBean();
//        return manager;
//    }
//
//    @Bean
//    public UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider() {
//        return new UsernamePasswordAuthenticationProvider();
//    }
//
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//        auth.authenticationProvider(usernamePasswordAuthenticationProvider());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception{
//        http.requestMatchers()
//                .antMatchers("/oauth/**")
//                .and()
//                .authenticationProvider(usernamePasswordAuthenticationProvider());
//    }
//
//}
