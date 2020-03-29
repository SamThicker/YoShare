package com.yo.fileservice.service.ServiceImpl;

import com.yo.fileservice.service.FileService;
import com.yo.fileservice.components.MinioTemplate;
import com.yo.yoshare.common.api.CommonResult;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

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
    public String presignedPutUrl(String bucketName, String objectName, Integer expires) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidExpiresRangeException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return minioTemplate.presignedPutObjectUrl(bucketName, objectName, null).replaceFirst("^.+:[0-9]+","");
    }

    @Override
    public String presignedBlogFilePutUrl(Long userId, String fileName, int expires) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidExpiresRangeException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {
        String objectName = "blogPic/"+ userId + "-" +UUID.randomUUID()+fileName.substring(fileName.lastIndexOf("."));
        return minioTemplate.presignedPutObjectUrl("resource", objectName, null).replaceFirst("^.+:[0-9]+","");
    }

}
