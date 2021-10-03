package com.jerry.savior_oauth.filters.tokens;

import com.jerry.savior_common.error.BusinessException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Set;

/**
 * @author 22454
 */
public class DynamicAuthenticationToken extends AbstractAuthenticationToken {
    private static final Long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    private final Object principal;
    private Object credentials;
    private Set<Integer> permissions;


    public DynamicAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    public DynamicAuthenticationToken(Object principal,
                                      Object credentials,
                                      Set<Integer> permissions) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.permissions=permissions;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    @Deprecated
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        throw new RuntimeException("该权限集合已弃用");
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }

    public Set<Integer> getPermissions() {
        return permissions;
    }
}
