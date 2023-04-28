package com.yo.yoshare.common.db;

import lombok.Data;

import java.util.Date;

@Data
public class TableModelTemplate {
    /**
     * 乐观锁
     */
    protected Integer revision;
    /**
     * 注册时间
     */
    protected Date registeredTime;
    /**
     * 创建人
     */
    protected String createdBy;
    /**
     * 创建时间
     */
    protected Date createdTime;
    /**
     * 更新人
     */
    protected String updatedBy;
    /**
     * 更新时间
     */
    protected Date updatedTime;

    public TableModelTemplate() {
        revision = 0;
        registeredTime = new Date();
        createdBy = "System";
        createdTime = registeredTime;
        updatedBy = createdBy;
        updatedTime = registeredTime;
    }
}
