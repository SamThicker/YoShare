package com.yo.yoshare.memberservice.controller;

import com.netflix.client.http.HttpResponse;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.memberservice.dto.UmsMemberExistResult;
import com.yo.yoshare.memberservice.dto.UmsMemberSimpleInfo;
import com.yo.yoshare.memberservice.dto.UmsMemberRegisterParam;
import com.yo.yoshare.memberservice.service.AreaService;
import com.yo.yoshare.memberservice.service.MemberService;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Api(tags = "MemberController", description = "会员管理")
@RestController
public class MemberController {
    @Autowired
    MemberService memberService;
    @Autowired
    AreaService areaService;

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public CommonResult<UmsMemberSimpleInfo> register(@RequestBody @Valid UmsMemberRegisterParam umsMemberRegisterParam, BindingResult result) throws Exception {
        return memberService.register(umsMemberRegisterParam);
    }

    @ApiOperation(value = "查询用户信息")
    @RequestMapping(value = "/{param}/info/{option}", method = RequestMethod.GET)
    public CommonResult<UmsMemberSimpleInfo> getMemberInfo(@PathVariable("param") String param,
                                                           @PathVariable("option") String option) {
        if (param != null) {
            UmsMemberSimpleInfo umsMemberInfo = memberService.getMemberInfo(param,option);
            if (umsMemberInfo != null) {
                return CommonResult.success(umsMemberInfo);
            }
        }
        return CommonResult.failed("客官，没有您要找的东西");
    }

    @ApiOperation(value = "更新用户信息")
    @RequestMapping(value = "/{id}/info", method = RequestMethod.PUT)
    public CommonResult<UmsMemberSimpleInfo> updateMemberInfo(@PathVariable("id") Long id,
                                                              @RequestBody @Valid UmsMemberSimpleInfo newInfo, String tt) {
        if (id != null) {
            newInfo.setId(id);
            UmsMemberSimpleInfo umsMemberInfo = memberService.updateMemberInfo(newInfo);
            if (umsMemberInfo != null) {
                return CommonResult.success(umsMemberInfo);
            }
        }
        return CommonResult.failed("客官，出错了惹");
    }

    @ApiOperation(value = "查询凭证是否已用于注册")
    @RequestMapping(value = "/info/exist", method = RequestMethod.POST)
    public CommonResult<UmsMemberExistResult> isExist(@RequestParam(name = "username", required = false) String username,
                                                      @RequestParam(name = "phone", required = false) String phone,
                                                      @RequestParam(name = "email", required = false) String email) {
        return CommonResult.success(memberService.isExist(username, phone, email));
    }

    @ApiOperation(value = "上传用户头像")
    @RequestMapping(value = "/{id}/icon", method = RequestMethod.POST)
    public CommonResult<String> iconUpload(@RequestParam(value = "file") MultipartFile file,
                                           @PathVariable("id") Long id) {
        if (id == null) {
            return CommonResult.failed("抱歉，您没有权限");
        }
        if (file.isEmpty()) {
            return CommonResult.failed("文件为空");
        }
        Boolean result = memberService.addIcon(file, id);
        if (result) {
            return CommonResult.success("操作成功");
        } else {
            return CommonResult.failed("操作失败");
        }
    }


    @ApiOperation(value = "获取上传用户头像的url")
    @GetMapping(value = "/{id}/avatarUploadUrl")
    public CommonResult<String> getIconUploadDir(@PathVariable("id") Long id, String type) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, InvalidExpiresRangeException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException {
        if (id == null) {
            return CommonResult.failed("抱歉，您没有权限");
        }
        return memberService.getIconUploadDir(id, type);
    }

    @ApiOperation(value = "生成邮箱验证码并发邮件")
    @RequestMapping(value = "/mailVerificationCode", method = RequestMethod.POST)
    public CommonResult<String> generateMailVerifyCode(HttpServletResponse response, @RequestParam String mail){
        return memberService.generateMailVerifyCode(response, mail);
    }

    @ApiOperation(value = "获取用户头像")
    @RequestMapping(value = "/{id}/icon", method = RequestMethod.GET, produces = {"image/png"})
    public BufferedImage getIcon(@PathVariable("id") Long id) throws IOException {
        String iconDir = memberService.getIconDir(id);
        FileInputStream file = new FileInputStream(iconDir);
        return ImageIO.read(file);
    }



    @Bean
    public BufferedImageHttpMessageConverter bufferedImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

    @ApiOperation(value = "获取地区")
    @RequestMapping(value = "area/{code}", method = RequestMethod.GET)
    public CommonResult getProvince(@PathVariable int code) throws IOException {
        Object object = areaService.getAreaByCode(code);
        if (object == null){
            return CommonResult.failed("阿欧，出错辣~");
        }
        return CommonResult.success(object,"操作成功");
    }
}
