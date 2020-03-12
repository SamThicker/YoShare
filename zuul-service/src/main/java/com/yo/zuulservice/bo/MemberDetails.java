package com.yo.zuulservice.bo;

import com.yo.yoshare.mbg.model.UmsMemberSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class MemberDetails implements UserDetails {

    private UmsMemberSecurity umsMemberSecurity;

    public MemberDetails(UmsMemberSecurity umsMemberSecurity) {
        this.umsMemberSecurity = umsMemberSecurity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
        return !umsMemberSecurity.getStatus().equals(1);
    }
}
