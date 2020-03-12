package com.yo.resourcesrevice.service.ServiceImpl;

import com.yo.resourcesrevice.service.ResourceService;
import com.yo.resourcesrevice.components.MinioTemplate;
import com.yo.yoshare.common.api.CommonResult;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

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

    @Autowired
    private MinioTemplate minioTemplate;

    @Override
    public CommonResult uploadResource(String username, MultipartFile file) throws Exception {
        minioTemplate.putObject("static","icon/a",file.getInputStream());
        return CommonResult.success("","");
    }


    @Override
    public Object presignedPutUrl(String bucketName, String objectName, Integer expires) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidExpiresRangeException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return minioTemplate.presignedPutObjectUrl(bucketName, objectName, expires).replaceFirst("^.+:[0-9]+","");
    }

}
