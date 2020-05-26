package com.yo.fileservice.controller;

import com.yo.fileservice.service.GroupFileService;
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
                                   @RequestParam("file") MultipartFile file,
                                   @RequestParam("title") String title,
                                   @RequestParam("description") String description,
                                   @RequestParam("classis") Optional<Long> classis,
                                   @RequestParam("hash") String hash) throws Exception {
        return groupFileService.uploadFile(null, groupId, hash, file, title, description, classis);
    }

    @PutMapping(value = "/group/{groupId}/existFile")
    public CommonResult uploadFile(@PathVariable(value = "groupId") Long groupId,
                                   @RequestParam("title") String title,
                                   @RequestParam("description") String description,
                                   @RequestParam("classis") Optional<Long> classis,
                                   @RequestParam("hash") String hash,
                                   @RequestParam("name") String name) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlPullParserException, InvalidExpiresRangeException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        return groupFileService.uploadExistFile(null, groupId, name, hash, title, description, classis);
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
