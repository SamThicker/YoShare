package com.yo.yoshare.memberservice.service.impl;

import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.common.db.MemberSecurityInfoTemplate;
import com.yo.yoshare.common.db.MemberSimpleInfoTemplate;
import com.yo.yoshare.common.model.ModelBuilder;
import com.yo.yoshare.common.security.SecurityUtil;
import com.yo.yoshare.mbg.mapper.UmsMemberInfoMapper;
import com.yo.yoshare.mbg.mapper.UmsMemberSecurityMapper;
import com.yo.yoshare.mbg.model.UmsMemberInfo;
import com.yo.yoshare.mbg.model.UmsMemberSecurity;
import com.yo.yoshare.mbg.model.UmsMemberSecurityExample;
import com.yo.yoshare.memberservice.dto.UmsMemberExistResult;
import com.yo.yoshare.memberservice.dto.UmsMemberSimpleInfo;
import com.yo.yoshare.memberservice.dto.UmsMemberRegisterParam;
import com.yo.yoshare.memberservice.feign.FileService;
import com.yo.yoshare.memberservice.feign.NotificationService;
import com.yo.yoshare.memberservice.service.MemberService;
import io.minio.errors.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    RedisServiceImpl redisService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired(required = false)
    /**存放用户基本信息*/
    UmsMemberInfoMapper umsMemberInfoMapper;
    @Autowired(required = false)
    /**存放用户安全数据、如密码、手机号等*/
    UmsMemberSecurityMapper umsMemberSecurityMapper;
    @Autowired
    FileService fileService;
    @Autowired
    NotificationService notificationService;
    /**用户头像路径*/
    @Value("${filesystem.icon}")
    private String MEMBER_ICON_DIR;
    /**注册验证码redis前缀*/
    @Value("${redis.key.prefix.registerAuthCode}")
    private String REDIS_KEY_PREFIX_REGISTER_AUTH;
    /**注册验证码过期时间*/
    @Value("${redis.key.expire.registerAuthCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;
    /**限制获取邮箱验证码的间隔*/
    @Value("${redis.key.timeout.registerAuthCodeMail}")
    private Long REGISTER_AUTH_CODE_MAIL_TIME_OUT;
    /**头像前缀*/
    @Value("${web.icon}")
    private String ICON_URL_PREFIX;

    @Override
    public CommonResult register(UmsMemberRegisterParam umsMemberRegisterParam) throws Exception {
        //查看用户名和邮箱是否被注册
        if (isExist(umsMemberRegisterParam.getUsername(),null, umsMemberRegisterParam.getEmail()).getExist()) {
            return CommonResult.failed("用户名或邮箱已被注册");
        }
        //查看验证码是否正确
        String correctCode = redisService.get(REDIS_KEY_PREFIX_REGISTER_AUTH+umsMemberRegisterParam.getEmail());
        if (!umsMemberRegisterParam.getVerificationCode().equals(correctCode)){
            return CommonResult.failed("验证码不正确");
        }
        //用户基本信息
        UmsMemberInfo umsMemberInfo = null;
        //用户安全数据
        UmsMemberSecurity umsMemberSecurity = null;
        try {
            //buildModel用于添加创建时间、更新时间等与业务不太相关的信息
            umsMemberInfo = ModelBuilder.buildModel(new UmsMemberInfo(), umsMemberRegisterParam, MemberSimpleInfoTemplate.class);
            umsMemberSecurity = ModelBuilder.buildModel(new UmsMemberSecurity(), umsMemberRegisterParam, MemberSecurityInfoTemplate.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (umsMemberInfo == null || umsMemberSecurity == null) {
            return CommonResult.failed("阿欧，出错辣~");
        }
        //对密码进行加密
        String encodedPassword = passwordEncoder.encode(umsMemberSecurity.getPassword());
        umsMemberSecurity.setPassword(encodedPassword);
        //插入基本信息
        umsMemberInfoMapper.insertSelective(umsMemberInfo);
        umsMemberSecurity.setId(umsMemberInfo.getId());
        //插入安全信息
        umsMemberSecurityMapper.insertSelective(umsMemberSecurity);
        redisService.remove(REDIS_KEY_PREFIX_REGISTER_AUTH+umsMemberRegisterParam.getEmail());
        //返回用户视图的用户信息，不包括乐观锁等字段
        UmsMemberSimpleInfo info = new UmsMemberSimpleInfo();
        BeanUtils.copyProperties(umsMemberInfo, info);
        return CommonResult.success(info, "注册成功");
    }


    @Override
    public UmsMemberSimpleInfo getMemberInfo(String param, String option) {
        UmsMemberSecurityExample example = new UmsMemberSecurityExample();
        switch (option){
            case "username":
                example.createCriteria().andUsernameEqualTo(param);
                break;
            case "id":
                example.createCriteria().andIdEqualTo(Long.parseLong(param));
                break;
            case "mail":
                example.createCriteria().andEmailEqualTo(param);
                break;
            case "account":
                String mailRegExp = "^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$";
                if (param.matches(mailRegExp)){
                    example.createCriteria().andEmailEqualTo(param);
                }else {
                    example.createCriteria().andUsernameEqualTo(param);
                }
                break;
            default:
                return null;
        }
        List<UmsMemberSecurity> securityInfoList = umsMemberSecurityMapper.selectByExample(example);
        if (securityInfoList.size() == 0) {
            return null;
        }
        UmsMemberInfo umsMemberInfo = umsMemberInfoMapper.selectByPrimaryKey(securityInfoList.get(0).getId());
        if (umsMemberInfo != null) {
            umsMemberInfo.setIcon(ICON_URL_PREFIX + umsMemberInfo.getIcon());
            UmsMemberSimpleInfo info = new UmsMemberSimpleInfo();
            BeanUtils.copyProperties(umsMemberInfo, info);
            return info;
        }
        return null;
    }


    public UmsMemberSecurity getMemberSecurityInfoByMail(String mail) {
        if (mail != null) {
            UmsMemberSecurityExample example = new UmsMemberSecurityExample();
            example.createCriteria().andEmailEqualTo(mail);
            List <UmsMemberSecurity> list = umsMemberSecurityMapper.selectByExample(example);
            return list.size()==0
                   ? null
                   :list.get(0);
        }
        return null;
    }

    @Override
    public UmsMemberExistResult isExist(String username, String phone, String email) {
        UmsMemberExistResult result = new UmsMemberExistResult();
        UmsMemberSecurityExample example = new UmsMemberSecurityExample();
        if (username != null) {
            example.createCriteria().andUsernameEqualTo(username);
            if (umsMemberSecurityMapper.selectByExample(example).size() > 0) {
                result.setExist(true);
                result.setUsername(true);
            }
        }
        example.clear();
        if (phone != null) {
            example.createCriteria().andPhoneEqualTo(phone);
            if (umsMemberSecurityMapper.selectByExample(example).size() > 0) {
                result.setExist(true);
                result.setPhone(true);
            }
        }
        example.clear();
        if (email != null) {
            example.createCriteria().andEmailEqualTo(email);
            if (umsMemberSecurityMapper.selectByExample(example).size() > 0) {
                result.setExist(true);
                result.setEmail(true);
            }
        }
        return result;
    }



    @Override
    public UmsMemberSimpleInfo updateMemberInfo(UmsMemberSimpleInfo newInfo) {
        UmsMemberInfo info = new UmsMemberInfo();
        BeanUtils.copyProperties(newInfo, info);
        info.setUpdatedTime(new Date());
        if (1 == umsMemberInfoMapper.updateByPrimaryKeySelective(info)) {
            return getMemberInfo(info.getId()+"","id");
        }
        return null;
    }

    @Override
    public Boolean addIcon(MultipartFile file, Long id) {
        // 文件名
        String fileName = file.getOriginalFilename();
        // 后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 上传后的路径
        String filePath = MEMBER_ICON_DIR;
        // 新文件名
        fileName = id + "_ORIGINAL" + suffixName;
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        UmsMemberInfo info = new UmsMemberInfo();
        info.setIcon(fileName);
        info.setId(id);
        return 1 == umsMemberInfoMapper.updateByPrimaryKeySelective(info);
    }

    @Override
    public String getIconDir(Long id) {
        String parrentDir = MEMBER_ICON_DIR;
        String fileName = "default";
        UmsMemberInfo info = umsMemberInfoMapper.selectByPrimaryKey(id);
        if (info != null) {
            fileName = info.getIcon();
        }
        return parrentDir + fileName;
    }

    @Override
    public CommonResult<String> generateMailVerifyCode(HttpServletResponse response, String mail) {
        UmsMemberSecurity securityInfo = getMemberSecurityInfoByMail(mail);
        if (securityInfo != null){
            return CommonResult.failed("该邮箱已被注册");
        }
        Long expireTime = redisService.getExpire(REDIS_KEY_PREFIX_REGISTER_AUTH+mail);
        //判断邮件发送时间是否大于设定的间隔
        if (expireTime!=null && (AUTH_CODE_EXPIRE_SECONDS-expireTime) < REGISTER_AUTH_CODE_MAIL_TIME_OUT){
            return CommonResult.failed("操作过于频繁");
        }
        String verifyCode = SecurityUtil.generateCheckcode(6);
        System.out.println("code:"+verifyCode);
        redisService.set(REDIS_KEY_PREFIX_REGISTER_AUTH + mail, verifyCode);
        redisService.expire(REDIS_KEY_PREFIX_REGISTER_AUTH +mail,AUTH_CODE_EXPIRE_SECONDS);
        notificationService.sendMail(mail,"YoShare注册验证码","","注册账号",verifyCode);
        return CommonResult.success("验证码已发送", "验证码已发送");
    }

    @Override
    public CommonResult getIconUploadDir(Long id, String type) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidPortException, InvalidExpiresRangeException, InternalException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, ErrorResponseException {
        // 后缀名
        String suffixName = "." + type;
        // 文件名
        String fileName = id + "_ORIGINAL" + suffixName;
        UmsMemberInfo info = new UmsMemberInfo();
        info.setIcon(fileName);
        info.setId(id);
        if (1 != umsMemberInfoMapper.updateByPrimaryKeySelective(info)) {
            return CommonResult.failed("操作失败，请重试");
        }
        return fileService.getIconUploadUrl( "icon/" + fileName );
    }

}
