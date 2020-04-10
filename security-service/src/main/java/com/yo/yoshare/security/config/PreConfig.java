//package com.yo.yoshare.security.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.provider.ClientDetailsService;
//import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class PreConfig {
//
//    @Autowired
//    DataSource dataSource;
//
//    @Bean(name = "aa")
//    public ClientDetailsService clientDetails() {
//        return new JdbcClientDetailsService(dataSource);//配置客户端
//    }
//
//}
