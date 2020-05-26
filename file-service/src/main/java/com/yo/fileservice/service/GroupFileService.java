package com.yo.fileservice.service;

import com.yo.yoshare.common.api.CommonResult;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface GroupFileService {

    CommonResult uploadFile(Long id, Long groupId, String hash, MultipartFile file, String title, String description, Optional<Long> classis) throws Exception;

    CommonResult uploadExistFile(Long id, Long groupId, String name, String hash, String title, String description, Optional<Long> classis) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidExpiresRangeException, InvalidResponseException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException;

    CommonResult deleteFile(String fileId, String groupId) throws Exception;

    CommonResult getFileInfo(String fileId, String goroupId);
}
