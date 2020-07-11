package com.yo.fileservice.service;

import com.yo.fileservice.vo.VOFileResourceInfo;
import com.yo.fileservice.vo.VOFileTransInfo;
import com.yo.yoshare.common.api.CommonResult;
import io.minio.errors.*;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface GroupFileService {

    CommonResult uploadFile(VOFileResourceInfo info, Long groupId) throws Exception;

    CommonResult uploadExistFile(Long groupId, VOFileResourceInfo info) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidExpiresRangeException, InvalidResponseException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException;

    CommonResult deleteFile(String fileId, String groupId) throws Exception;

    CommonResult getFileInfo(String fileId, String goroupId);

    CommonResult uploadMultipartFile(VOFileTransInfo info, Long groupId) throws Exception;

    void deleteFileInMinio(String user, String param1, String param2) throws Exception;
}
