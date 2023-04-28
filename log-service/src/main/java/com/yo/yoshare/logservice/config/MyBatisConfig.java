package com.yo.yoshare.logservice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan({"com.yo.yoshare.mbg.mapper", "com.yo.yoshare.logservice.dao"})
public class MyBatisConfig {
}
