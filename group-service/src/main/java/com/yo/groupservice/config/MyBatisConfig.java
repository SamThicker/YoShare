package com.yo.groupservice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan({"com.yo.yoshare.mbg.mapper", "com.yo.groupservice.dao"})
public class MyBatisConfig {
}
