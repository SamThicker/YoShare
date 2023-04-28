package com.yo.yoshare.security.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan({"com.yo.yoshare.mbg.mapper", "com.yo.yoshare.memberservice.dao"})
public class MyBatisConfig {
}
