package com.yo.yoshare.common.db;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MemberSecurityInfoTemplate extends TableModelTemplate {
    /**
     * 账号状态、仅ums_member_info表有效
     * "0"->注销
     * "1"->启用
     * "2"->未验证邮箱
     */
    protected String status;

    public MemberSecurityInfoTemplate() {
        super();
        status = "2";
    }
}
