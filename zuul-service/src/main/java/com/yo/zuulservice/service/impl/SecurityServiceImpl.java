package com.yo.zuulservice.service.impl;

import com.yo.yoshare.mbg.mapper.UmsMemberSecurityMapper;
import com.yo.yoshare.mbg.model.UmsMemberSecurity;
import com.yo.yoshare.mbg.model.UmsMemberSecurityExample;
import com.yo.zuulservice.bo.MemberDetails;
import com.yo.zuulservice.service.SecurityService;
import com.yo.zuulservice.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    UmsMemberSecurityMapper umsMemberSecurityMapper;



    @Override
    public UmsMemberSecurity getUserByUsername(String username) {
        List<UmsMemberSecurity> umsMemberSecurityList;
        UmsMemberSecurityExample example = new UmsMemberSecurityExample();
        example.createCriteria().andUsernameEqualTo(username);
        umsMemberSecurityList = umsMemberSecurityMapper.selectByExample(example);
        return umsMemberSecurityList.size() > 0 ? umsMemberSecurityList.get(0) : null;
    }

    @Override
    public UmsMemberSecurity getUserById(Long id) {
        List<UmsMemberSecurity> umsMemberSecurityList;
        UmsMemberSecurityExample example = new UmsMemberSecurityExample();
        example.createCriteria().andIdEqualTo(id);
        umsMemberSecurityList = umsMemberSecurityMapper.selectByExample(example);
        return umsMemberSecurityList.size() > 0 ? umsMemberSecurityList.get(0) : null;
    }
}
