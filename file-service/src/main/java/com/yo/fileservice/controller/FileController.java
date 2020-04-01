package com.yo.fileservice.controller;

import com.yo.fileservice.service.FileService;
import com.yo.yoshare.common.api.CommonResult;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
public class FileController {
    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public CommonResult uploadResource(String username, MultipartFile file, @PathVariable("directly") boolean directly) throws Exception {
        return fileService.uploadResource(username, file);
    }

    @RequestMapping(value = "/uploadUrl/{name}", method = RequestMethod.GET)
    public CommonResult getUploadUrl(@PathVariable("name") String name) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, InvalidExpiresRangeException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException {
        return CommonResult.success(fileService.presignedPutUrl("resource",name,180),"操作成功");
    }

    @RequestMapping(value = "/blogFilePutUrl/{id}/{filename}", method = RequestMethod.GET)
    public CommonResult getFilePutUrl(@PathVariable("id") Long userid,
                                      @PathVariable("filename") String fileName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, InvalidExpiresRangeException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException {
        return CommonResult.success(fileService.presignedBlogFilePutUrl(userid, fileName ,360),"操作成功");
    }

    @RequestMapping(value = "/iconUploadUrl", method = RequestMethod.GET)
    public CommonResult getIconUploadUrl(@RequestParam String name) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, InvalidExpiresRangeException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException {
        return CommonResult.success(fileService.presignedPutUrl("static",name,180),"操作成功");
    }

    @RequestMapping(value = "/groupAvatarUploadUrl", method = RequestMethod.GET)
    public CommonResult getGroupAvatarUploadUrl(@RequestParam String name) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, InvalidExpiresRangeException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException {
        return CommonResult.success(fileService.presignedPutUrl("static",name,180),"操作成功");
    }
}
