package com.yo.yoshare.memberservice.service;

import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.memberservice.dto.UmsMemberExistResult;
import com.yo.yoshare.memberservice.dto.UmsMemberSimpleInfo;
import com.yo.yoshare.memberservice.dto.UmsMemberRegisterParam;/*
import org.springframework.security.core.userdetails.UserDetails;*/
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface MemberService {

    /**
     * 用于用户注册
     */
    CommonResult register(UmsMemberRegisterParam umsMemberParam) throws Exception;

    /**
     * 获取用户的信息
     */
    UmsMemberSimpleInfo getMemberInfo(String param, String option);

    /**
     * 判断用户安全信息是否已经被注册
     */
    UmsMemberExistResult isExist(String username, String phone, String email);

    /**
     * 更新用户信息
     */
    UmsMemberSimpleInfo updateMemberInfo(UmsMemberSimpleInfo newInfo);

    /**
     * 上传头像
     */
    Boolean addIcon(MultipartFile file, Long id);

    /**
     * 获取头像地址
     */
    String getIconDir(Long id);

    CommonResult<String> generateMailVerifyCode(HttpServletResponse response, String mail);

    CommonResult getIconUploadDir(Long id, String type) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidPortException, InvalidExpiresRangeException, InternalException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, ErrorResponseException;
}
