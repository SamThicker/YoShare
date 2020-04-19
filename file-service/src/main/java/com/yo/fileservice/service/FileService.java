package com.yo.fileservice.service;

import com.yo.yoshare.common.api.CommonResult;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface FileService {

    /**
     * 直接上传文件
     * */
    CommonResult uploadResource(String username, MultipartFile file) throws Exception;


    /**
     * 获取浏览器上传的url
     * */
    String presignedPutUrl(String bucketName, String objectName, Integer expires) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidExpiresRangeException, InternalException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, InvalidResponseException, XmlParserException;

    /**
     * 获取浏览器上传博客图片的url
     * */
    Object presignedBlogFilePutUrl(Long userid,String fileName, int expires) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidExpiresRangeException, ErrorResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, InvalidResponseException, XmlParserException;

    CommonResult uploadFile(Long id, String hash, MultipartFile file, String title, String description, Optional<String> classis) throws NoSuchAlgorithmException, IOException;

    CommonResult uploadExistFile(Long id, String name, String hash, String title, String description, Optional<String> classis);
}
