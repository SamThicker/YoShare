package com.yo.fileservice.controller;

import com.yo.fileservice.service.GroupFileService;
import com.yo.yoshare.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
                                   @RequestParam("hash") String hash) throws IOException, NoSuchAlgorithmException {
        return groupFileService.uploadFile(null, groupId, hash, file, title, description, classis);
    }

    @PutMapping(value = "/group/{groupId}/existFile")
    public CommonResult uploadFile(@PathVariable(value = "groupId") Long groupId,
                                   @RequestParam("title") String title,
                                   @RequestParam("description") String description,
                                   @RequestParam("classis") Optional<Long> classis,
                                   @RequestParam("hash") String hash,
                                   @RequestParam("name") String name) throws IOException, NoSuchAlgorithmException {
        return groupFileService.uploadExistFile(null, groupId, name, hash, title, description, classis);
    }

}
