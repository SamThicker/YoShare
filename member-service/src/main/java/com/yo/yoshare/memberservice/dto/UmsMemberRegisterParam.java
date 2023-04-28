package com.yo.yoshare.memberservice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class UmsMemberRegisterParam {

    @ApiModelProperty(value = "用户名 6~20字符，仅数字和字母")
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码 数字+字符，20字符内")
    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度介于6和20之间")
    private String password;

    @NotEmpty(message = "邮箱不能为空")
    @ApiModelProperty(value = "邮箱")
    @Email
    private String email;

    @NotEmpty(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码")
    @Length(min = 6, max = 6, message = "格式不正确，验证码为6位数字")
    private String verificationCode;

}
