package com.yo.fileservice.controller;

import com.yo.fileservice.service.GroupFileService;
import com.yo.fileservice.vo.VOFileResourceInfo;
import com.yo.fileservice.vo.VOFileTransInfo;
import com.yo.yoshare.common.api.CommonResult;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
@RequestMapping("/file")
public class GroupFileController {

    @Autowired
    private GroupFileService groupFileService;

    @PutMapping(value = "/group/{groupId}/file")
    public CommonResult uploadFile(@PathVariable(value = "groupId") Long groupId,
                                   VOFileResourceInfo info) throws Exception {
        return groupFileService.uploadFile(info, groupId);
    }

    /**上传文件分片*/
    @PostMapping(value = "/group/{groupId}/multipartFile")
    public CommonResult uploadMutipartFile(@PathVariable("groupId") Long groupId,
                                           VOFileTransInfo info) throws Exception {
        return groupFileService.uploadMultipartFile(info,groupId);
    }

    @PutMapping(value = "/group/{groupId}/existFile")
    public CommonResult uploadExistFile(@PathVariable(value = "groupId") Long groupId,
                                   VOFileResourceInfo info) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlPullParserException, InvalidExpiresRangeException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        return groupFileService.uploadExistFile(groupId, info);
    }

    @ResponseBody
    @DeleteMapping(value = "/group/{groupId}/file/{fileId}")
    public CommonResult deleteFile(@PathVariable(value = "fileId") String fileId,
                                   @PathVariable(value = "groupId") String groupId) throws Exception {
        return groupFileService.deleteFile(fileId, groupId);
    }

    @GetMapping(value = "/group/{groupId}/fileInfo/{fileId}")
    public CommonResult getFileInfo(@PathVariable(value = "fileId") String fileId,
                                    @PathVariable(value = "groupId") String groupId) throws IOException, ServletException {
        return groupFileService.getFileInfo(fileId, groupId);
    }

}
