package com.yo.fileservice.service;

import com.yo.fileservice.vo.VOFileResourceInfo;
import com.yo.fileservice.vo.VOFileTransInfo;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.model.CmsFileTransInfo;
import com.yo.yoshare.mbg.model.CmsMemberFile;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    CommonResult uploadFile(VOFileResourceInfo info) throws Exception;

    CommonResult uploadExistFile(Long id, VOFileResourceInfo info) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidExpiresRangeException, InvalidResponseException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException;

    void downloadFile(HttpServletRequest req, HttpServletResponse resp, Long userId, String fileId) throws IOException, ServletException;

    /**获取文件信息*/
    CommonResult getFileInfo(String fileId);

    CommonResult deleteFile(String fileId) throws Exception;

    CommonResult uploadMultipartFile(VOFileTransInfo info) throws Exception;
}
