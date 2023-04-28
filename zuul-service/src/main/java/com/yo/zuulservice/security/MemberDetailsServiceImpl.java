package com.yo.zuulservice.security;

import com.yo.yoshare.mbg.mapper.UmsMemberSecurityMapper;
import com.yo.yoshare.mbg.model.UmsMemberSecurity;
import com.yo.yoshare.mbg.model.UmsMemberSecurityExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MemberDetailsServiceImpl implements MemberDetailsService {

    @Autowired
    UmsMemberSecurityMapper umsMemberSecurityMapper;

    @Override
    public UserDetails loadUserByAccount(String account) {
        String mailRegExp = "^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$";
        UmsMemberSecurityExample example = new UmsMemberSecurityExample();
        if (account.matches(mailRegExp)){
            return loadUserByMail(account);
        }
        return loadUserByUsername(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UmsMemberSecurityExample example = new UmsMemberSecurityExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsMemberSecurity> list = umsMemberSecurityMapper.selectByExample(example);
        //如果查询到结果，则取值，否则为null
        UmsMemberSecurity result = list.size()>0 ? list.get(0) : null;
        return result!=null ? new JwtMemberDetails(result.getUsername(),result.getPassword(),result.getStatus()) : null;
    }

    @Override
    public UserDetails loadUserById(Long id) {
        UmsMemberSecurityExample example = new UmsMemberSecurityExample();
        example.createCriteria().andIdEqualTo(id);
        List<UmsMemberSecurity> list = umsMemberSecurityMapper.selectByExample(example);
        //如果查询到结果，则取值，否则为null
        UmsMemberSecurity result = list.size()>0 ? list.get(0) : null;
        return result!=null ? new JwtMemberDetails(result.getUsername(),result.getPassword(),result.getStatus()) : null;
    }

    @Override
    public UserDetails loadUserByMail(String mail) {
        UmsMemberSecurityExample example = new UmsMemberSecurityExample();
        example.createCriteria().andEmailEqualTo(mail);
        List<UmsMemberSecurity> list = umsMemberSecurityMapper.selectByExample(example);
        //如果查询到结果，则取值，否则为null
        UmsMemberSecurity result = list.size()>0 ? list.get(0) : null;
        return result!=null ? new JwtMemberDetails(result.getUsername(),result.getPassword(),result.getStatus()) : null;
    }


}
