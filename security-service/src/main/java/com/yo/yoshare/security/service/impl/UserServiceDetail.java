package com.yo.yoshare.security.service.impl;

import com.yo.yoshare.mbg.mapper.UmsMemberSecurityMapper;
import com.yo.yoshare.mbg.model.UmsMemberSecurity;
import com.yo.yoshare.mbg.model.UmsMemberSecurityExample;
import com.yo.yoshare.security.bo.MemberDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceDetail implements UserDetailsService {

    @Autowired
    UmsMemberSecurityMapper umsMemberSecurityMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UmsMemberSecurityExample example = new UmsMemberSecurityExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsMemberSecurity> list = umsMemberSecurityMapper.selectByExample(example);
        //如果查询到结果，则取值，否则为null
        UmsMemberSecurity result = list.size()>0 ? list.get(0) : null;
        return result!=null ? new MemberDetails(result) : null;
    }
}
