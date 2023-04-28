package com.yo.yoshare.security.service;

import com.yo.yoshare.mbg.model.UmsMemberSecurity;
import org.springframework.security.core.userdetails.UserDetails;

public interface SecurityService {

    /**
     * 用户登录，返回JWT
     */
    String login(String username, String password);

    /**
     * 返回用户信息，用于spring security
     */
    UserDetails loadPayloadByUsername(String username);

    /**
     * 根据用户名获取用户安全信息
     */
    UmsMemberSecurity getUserByUsername(String username);

    /**
     * 根据用户名ID获取用户安全信息
     */
    UmsMemberSecurity getUserById(Long id);
}
