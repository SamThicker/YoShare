package com.yo.yoshare.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {


    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }


    @Autowired
    MemberDetailsService memberDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //获取用户账号
        String account = authentication.getName();
        //获取用户安全数据
        UserDetails userDetails = memberDetailsService.loadUserByUsername(account);
        if (userDetails == null){
            throw new BadCredentialsException("账号或密码错误");
        }
        //检查用户账号状态（是否禁用）
        defaultCheck(userDetails);
        //检查凭证
        additionalAuthenticationChecks(userDetails, authentication);
        //如果运行到这一步，说明没有异常抛出，说明认证成功，接着构造已认证的authentication
        UsernamePasswordAuthenticationToken authenticatedToken = new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
        return authenticatedToken;
    }

    /**(附加检查项)检查密码是否正确*/
    private void additionalAuthenticationChecks(UserDetails userDetails,
                                                Authentication authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException("Bad credentials");
        }
        String presentedPassword = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }
    }

    /**用户默认检查,用户是否锁定过期等*/
    private void defaultCheck(UserDetails user) {
        if (!user.isAccountNonLocked()) {
            throw new LockedException("User account is locked");
        }
        if (!user.isEnabled()) {
            throw new DisabledException("User is disabled");
        }
        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("User account has expired");
        }
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) throws Exception {
        if (userDetailsService instanceof MemberDetailsService){
            this.memberDetailsService = (MemberDetailsService) userDetailsService;
            return;
        }
        throw new Exception("DetailsService type error, need type of 'MemberDetailsService'");
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }




}
