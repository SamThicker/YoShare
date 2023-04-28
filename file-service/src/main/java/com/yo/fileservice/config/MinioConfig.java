package com.yo.fileservice.config;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.accessKey}")
    private String accesskey;
    @Value("${minio.secretKey}")
    private String secretkey;

    @Bean
    public MinioClient client() throws InvalidPortException, InvalidEndpointException {
        return new MinioClient(endpoint, accesskey, secretkey);
    }

}
