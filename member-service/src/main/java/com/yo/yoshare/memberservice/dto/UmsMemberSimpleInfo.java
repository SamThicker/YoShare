package com.yo.yoshare.memberservice.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

public class UmsMemberSimpleInfo implements Serializable {
    @Null
    @ApiModelProperty(value = "用户ID")
    private Long id;

    private String username;

    @ApiModelProperty(value = "昵称 32字符(16汉字)内")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String icon;

    @ApiModelProperty(value = "生日")
    private Date birthday;

    @ApiModelProperty(value = "性别 数字表示，0->女，1->男，2->保密")
    private String gender;

    @ApiModelProperty(value = "简介 1024字符(512汉字)内")
    private String introduction;

    @ApiModelProperty(value = "地区 精确到市")
    private String location;

    @Null
    @ApiModelProperty(value = "注册时间")
    private Date registeredTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getRegisteredTime() {
        return registeredTime;
    }

    public void setRegisteredTime(Date registeredTime) {
        this.registeredTime = registeredTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", nickname=").append(nickname);
        sb.append(", icon=").append(icon);
        sb.append(", birthday=").append(birthday);
        sb.append(", gender=").append(gender);
        sb.append(", introduction=").append(introduction);
        sb.append(", location=").append(location);
        sb.append(", registeredTime=").append(registeredTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}




