package com.yo.yoshare.memberservice.feign;

import com.yo.yoshare.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xmlpull.v1.XmlPullParserException;
import io.minio.errors.*;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@FeignClient(value = "SERVICE-FILE")
public interface FileService {

    @RequestMapping(value = "/iconUploadUrl", method = RequestMethod.GET)
    CommonResult getIconUploadUrl(@RequestParam String name) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, InvalidExpiresRangeException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException;


}
