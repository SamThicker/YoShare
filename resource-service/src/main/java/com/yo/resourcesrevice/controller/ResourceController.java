package com.yo.resourcesrevice.controller;

import com.yo.resourcesrevice.service.ResourceService;
import com.yo.yoshare.common.api.CommonResult;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value = "/uploadResource", method = RequestMethod.GET)
    public CommonResult uploadResource(String username, MultipartFile file, @PathVariable("directly") boolean directly) throws Exception {
        return resourceService.uploadResource(username, file);
    }
    

    @RequestMapping(value = "/uploadUrl/{name}", method = RequestMethod.GET)
    public CommonResult getUploadUrl(@PathVariable("name") String name) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, InvalidExpiresRangeException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException {
        return CommonResult.success(resourceService.presignedPutUrl("resource",name,180),"操作成功");
    }

}

