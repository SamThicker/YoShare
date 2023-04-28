package com.yo.zuulservice.service;

import com.yo.yoshare.mbg.model.UmsMemberSecurity;
import org.springframework.security.core.userdetails.UserDetails;

public interface SecurityService {


    /**
     * 根据用户名获取用户安全信息
     */
    UmsMemberSecurity getUserByUsername(String username);

    /**
     * 根据用户名ID获取用户安全信息
     */
    UmsMemberSecurity getUserById(Long id);
}
