package com.yo.zuulservice.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberDetailsService extends UserDetailsService {

    /**
     * 返回用户信息，用于spring security
     */
    UserDetails loadUserByAccount(String account);

    UserDetails loadUserByUsername(String username);

    UserDetails loadUserById(Long id);

    UserDetails loadUserByMail(String mail);

}
