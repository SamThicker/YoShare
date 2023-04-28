package com.yo.yoshare.memberservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@EnableDiscoveryClient
@EnableFeignClients
@EnableTransactionManagement
@SpringBootApplication
public class MemberServiceApplication {

    // 在类上加入 @Slf4j 注解并配合 lombok 可以省去这行代码
    /*private final Logger logger = LoggerFactory.getLogger(MemberServiceApplication.class);*/

    public static void main(String[] args) {
        log.info("服务'{}'启动...", "MemberService");
        SpringApplication.run(MemberServiceApplication.class, args);
    }

}
