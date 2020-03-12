package com.yo.resourcesrevice.service;

import com.yo.yoshare.common.api.CommonResult;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface ResourceService {

    /**
     * 直接上传文件
     * */
    CommonResult uploadResource(String username, MultipartFile file) throws Exception;


    /**
     * 获取浏览器上传的url
     * */
    Object presignedPutUrl(String bucketName, String objectName, Integer expires) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidExpiresRangeException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException;
}
