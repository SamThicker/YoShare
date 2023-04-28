package com.yo.yoshare.security.service.impl;

import com.yo.yoshare.mbg.mapper.UmsMemberSecurityMapper;
import com.yo.yoshare.mbg.model.UmsMemberSecurity;
import com.yo.yoshare.mbg.model.UmsMemberSecurityExample;
import com.yo.yoshare.security.bo.MemberDetails;
import com.yo.yoshare.security.service.SecurityService;
import com.yo.yoshare.security.utils.JwtTokenUtil;
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
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    UmsMemberSecurityMapper umsMemberSecurityMapper;
//    @Autowired
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }

    @Override
    public String login(String username, String password) {
        try {
            UserDetails userDetails = loadPayloadByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                return null;
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenUtil.generateToken(userDetails);
            System.out.println(token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UserDetails loadPayloadByUsername(String username) {
        UmsMemberSecurity umsMemberSecurity = getUserByUsername(username);
        if (umsMemberSecurity == null) {
            return null;
        }
        return new MemberDetails(umsMemberSecurity);
    }

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
