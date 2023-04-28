package com.yo.zuulservice.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    /**用户信息*/
    private final Object principal;
    /**用户凭证*/
    private Object credentials;

    /**
     * 未认证的Token，供提交用户认证使用。principal为用户名
     */
    public JwtAuthenticationToken(Object principal, Object credentials){
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }


    /**
     * 认证后生成Token的构造器，由Provider认证成功后调用。此时principal为UserDetails
     */
    public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
