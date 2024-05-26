package com.example.mywarehouse.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN, MOD;

    @Override
    public String getAuthority() {
        return name();
    }
}
