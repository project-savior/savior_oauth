package com.jerry.savior_oauth.filters;

import com.jerry.savior_oauth.filters.tokens.DynamicAuthenticationToken;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;

/**
 * @author 22454
 */
@Component
public class MyPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetUrl, Object permission) {
        try {
            DynamicAuthenticationToken dynamicAuthenticationToken = (DynamicAuthenticationToken) authentication;
            Long permissionId = (Long) permission;
            Set<Long> permissions = dynamicAuthenticationToken.getPermissions();
            return permissions.contains(permissionId);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
