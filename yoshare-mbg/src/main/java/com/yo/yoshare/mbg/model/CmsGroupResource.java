package com.yo.yoshare.mbg.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class CmsGroupResource implements Serializable {
    private Long id;

    private String title;

    private String description;

    @ApiModelProperty(value = "项目类型")
    private String type;

    @ApiModelProperty(value = "分类类别")
    private Long classification;

    private Long groupId;

    @ApiModelProperty(value = "创建者id")
    private Long byUserId;

    @ApiModelProperty(value = "创建者用户名")
    private String byUserName;

    @ApiModelProperty(value = "记录添加时间")
    private Date dateTime;

    @ApiModelProperty(value = "记录对应的category的id")
    private String resourceRef;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getClassification() {
        return classification;
    }

    public void setClassification(Long classification) {
        this.classification = classification;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getByUserId() {
        return byUserId;
    }

    public void setByUserId(Long byUserId) {
        this.byUserId = byUserId;
    }

    public String getByUserName() {
        return byUserName;
    }

    public void setByUserName(String byUserName) {
        this.byUserName = byUserName;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getResourceRef() {
        return resourceRef;
    }

    public void setResourceRef(String resourceRef) {
        this.resourceRef = resourceRef;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", title=").append(title);
        sb.append(", description=").append(description);
        sb.append(", type=").append(type);
        sb.append(", classification=").append(classification);
        sb.append(", groupId=").append(groupId);
        sb.append(", byUserId=").append(byUserId);
        sb.append(", byUserName=").append(byUserName);
        sb.append(", dateTime=").append(dateTime);
        sb.append(", resourceRef=").append(resourceRef);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}