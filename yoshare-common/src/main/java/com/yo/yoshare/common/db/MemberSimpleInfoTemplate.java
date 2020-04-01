package com.yo.yoshare.common.db;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class MemberSimpleInfoTemplate extends TableModelTemplate {

    /**昵称 32字符(16汉字)内*/
    protected String nickname;

    /**头像 用户头像文件名*/
    protected String icon;

    /**生日*/
    protected Date birthday;

    /**性别 数字表示，0->女，1->男*/
    protected String gender;

    /**简介 1024字符(512汉字)内*/
    protected String introduction;

    /**地区 精确到市*/
    protected String location;

    public MemberSimpleInfoTemplate() throws ParseException {
        super();
        nickname = "阿瓜";
        icon = "DEFAULT.png";
        birthday = new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01");
        gender = "0";
        introduction = "这个人太懒了，什么都没留下";
        location = "广东省-江门市-蓬江区";
    }
}
