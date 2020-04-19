package com.yo.yoshare.security.config;

import com.yo.yoshare.mbg.mapper.UmsMemberSecurityMapper;
import com.yo.yoshare.mbg.model.UmsMemberSecurity;
import com.yo.yoshare.mbg.model.UmsMemberSecurityExample;
import com.yo.yoshare.security.bo.MemberDetails;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * MemberDetailsService
 *
 * @author fengzheng 古时的风筝
 * @date 2019/3/30
 */
@Slf4j
//@Component(value = "memberDetailsService")
public class MemberDetailsService implements UserDetailsService {

    @Autowired
    private UmsMemberSecurityMapper memberSecurityMapper;

//    @Autowired
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    @Autowired
    private TokenStore redisTokenStore;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        log.info("usernameis:" + username);
//        // 查询数据库操作
//        if(!username.equals("admin")){
//            throw new UsernameNotFoundException("the user is not found");
//        }else{
//            // 用户角色也应在数据库中获取
//            String role = "ROLE_ADMIN";
//            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//            authorities.add(new SimpleGrantedAuthority(role));
//            // 线上环境应该通过用户名查询数据库获取加密后的密码
//            String password = passwordEncoder.encode("123456");
//            // 返回默认的 User
//            // return new org.springframework.security.core.userdetails.User(username,password, authorities);
//
//            // 返回自定义的 KiteUserDetails
//            User user = new User(username,password,authorities);
//           return user;
//        }
//    }




//    @Autowired
//    UmsMemberSecurityMapper umsMemberSecurityMapper;
//
//    @Override
//    public UserDetails loadUserByAccount(String account) {
//        String mailRegExp = "^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$";
//        UmsMemberSecurityExample example = new UmsMemberSecurityExample();
//        if (account.matches(mailRegExp)){
//            return loadUserByMail(account);
//        }
//        return loadUserByUsername(account);
//    }
//
    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("username is:" + username);
        UmsMemberSecurityExample example = new UmsMemberSecurityExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsMemberSecurity> list = memberSecurityMapper.selectByExample(example);
        //如果查询到结果，则取值，否则为null
        UmsMemberSecurity result = list.size()>0 ? list.get(0) : null;
        if (result == null ){
            throw new UsernameNotFoundException("the user is not found");
        }
        //用户角色也应在数据库中获取
        String role = "ROLE_ADMIN";
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        // 返回默认的 User
        // return new org.springframework.security.core.userdetails.User(username,password, authorities);
        // 返回自定义的 MemberUserDetails
        return new MemberDetails(result, null);
    }
//
//    @Override
//    public UserDetails loadUserById(Long id) {
//        UmsMemberSecurityExample example = new UmsMemberSecurityExample();
//        example.createCriteria().andIdEqualTo(id);
//        List<UmsMemberSecurity> list = umsMemberSecurityMapper.selectByExample(example);
//        //如果查询到结果，则取值，否则为null
//        UmsMemberSecurity result = list.size()>0 ? list.get(0) : null;
//        return result!=null ? new MemberDetails(result) : null;
//    }
//
//    @Override
//    public UserDetails loadUserByMail(String mail) {
//        UmsMemberSecurityExample example = new UmsMemberSecurityExample();
//        example.createCriteria().andEmailEqualTo(mail);
//        List<UmsMemberSecurity> list = umsMemberSecurityMapper.selectByExample(example);
//        //如果查询到结果，则取值，否则为null
//        UmsMemberSecurity result = list.size()>0 ? list.get(0) : null;
//        return result!=null ? new MemberDetails(result) : null;
//    }


}
