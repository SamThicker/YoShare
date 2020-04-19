package com.yo.yoshare.security.bo;

import com.yo.yoshare.mbg.model.UmsMemberSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemberDetails implements UserDetails {

    private UmsMemberSecurity umsMemberSecurity;

    private List<SimpleGrantedAuthority> authorities;

    public MemberDetails(UmsMemberSecurity umsMemberSecurity) {
        this.umsMemberSecurity = umsMemberSecurity;
    }

    public MemberDetails(UmsMemberSecurity umsMemberSecurity, List<SimpleGrantedAuthority> authorities) {
        this.umsMemberSecurity = umsMemberSecurity;
        if (authorities == null){
            return;
        }
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return umsMemberSecurity.getPassword();
    }

    @Override
    public String getUsername() {
        return umsMemberSecurity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "1".equals(umsMemberSecurity.getStatus());
    }
}
