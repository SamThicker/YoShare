package com.yo.zuulservice.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JwtMemberDetails implements UserDetails {

    private Long memberid;
    private String username;
    private String password;
    private String status;
    private List<SimpleGrantedAuthority> authorities;


    public JwtMemberDetails() {
    }

    public JwtMemberDetails(Long id, String username, String password, String status, String ... roles) {
        JwtMemberDetails details = new JwtMemberDetails(username,password,status,roles);
        details.setMemberid(id);
    }

    public JwtMemberDetails(String username, String password, String status, String ... roles) {
        this.username = username;
        this.password = password;
        this.status = status;
        this.authorities= Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }


    public Long getMemberid() {
        return this.memberid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !"3".equals(status);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setMemberid(Long id) { this.memberid = id; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(List<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }


}
