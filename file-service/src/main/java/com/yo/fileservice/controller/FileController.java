package com.yo.fileservice.controller;


import com.mongodb.CommandResult;
import com.yo.fileservice.service.FileService;
import com.yo.fileservice.vo.VOFileResourceInfo;
import com.yo.fileservice.vo.VOFileTransInfo;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.model.CmsFileTransInfo;
import com.yo.yoshare.mbg.model.CmsMemberFile;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public CommonResult uploadResource(String username, MultipartFile file, @PathVariable("directly") boolean directly) throws Exception {
        return fileService.uploadResource(username, file);
    }

    @RequestMapping(value = "/uploadUrl/{name}", method = RequestMethod.GET)
    public CommonResult getUploadUrl(@PathVariable("name") String name) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, InvalidExpiresRangeException, ErrorResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException, InvalidResponseException, XmlParserException {
        return CommonResult.success(fileService.presignedPutUrl("resource",name,180),"操作成功");
    }

    @RequestMapping(value = "/blogFilePutUrl/{id}/{filename}", method = RequestMethod.GET)
    public CommonResult getFilePutUrl(@PathVariable("id") Long userid,
                                      @PathVariable("filename") String fileName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, InvalidExpiresRangeException, ErrorResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException, InvalidResponseException, XmlParserException {
        return CommonResult.success(fileService.presignedBlogFilePutUrl(userid, fileName ,360),"操作成功");
    }

    @RequestMapping(value = "/iconUploadUrl", method = RequestMethod.GET)
    public CommonResult getIconUploadUrl(@RequestParam String name) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, InvalidExpiresRangeException, ErrorResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException, InvalidResponseException, XmlParserException {
        return CommonResult.success(fileService.presignedPutUrl("static",name,180),"操作成功");
    }

    @RequestMapping(value = "/groupAvatarUploadUrl", method = RequestMethod.GET)
    public CommonResult getGroupAvatarUploadUrl(@RequestParam String name) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, InvalidExpiresRangeException, ErrorResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException, InvalidResponseException, XmlParserException {
        return CommonResult.success(fileService.presignedPutUrl("static",name,180),"操作成功");
    }

    @PutMapping(value = "/member/{id}/file")
    public CommonResult uploadFile(@PathVariable(value = "id") Long id,
                                   VOFileResourceInfo info) throws Exception {
        return fileService.uploadFile(info);
    }

    @PutMapping(value = "/member/{id}/existFile")
    public CommonResult uploadExistFile(@PathVariable(value = "id") Long id,
                                    VOFileResourceInfo info) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlPullParserException, InvalidExpiresRangeException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        return fileService.uploadExistFile(id, info);
    }


    @GetMapping(value = "/member/file/{fileId}")
    public void download(HttpServletRequest req, HttpServletResponse resp,
                         @PathVariable(value = "fileId") String fileId) throws IOException, ServletException {
        String userId = req.getHeader("userId");
        fileService.downloadFile(req,resp, Long.valueOf(userId),fileId);
    }

    @ResponseBody
    @DeleteMapping(value = "/member/file/{fileId}")
    public CommonResult deleteFile(@PathVariable(value = "fileId") String fileId) throws Exception {
        return fileService.deleteFile(fileId);
    }

    /**获取用户文件信息*/
    @GetMapping(value = "/member/fileInfo/{fileId}")
    public CommonResult getFileInfo(@PathVariable(value = "fileId") String fileId) throws IOException, ServletException {
        return fileService.getFileInfo(fileId);
    }

    @PostMapping(value = "/member/multipartFile")
    public CommonResult uploadMutipartFile(VOFileTransInfo info) throws Exception {
        return fileService.uploadMultipartFile(info);
    }
}

