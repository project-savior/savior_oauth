package com.jerry.savior_oauth.filters.tokens;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * @author 22454
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final Long userId;
    private final String token;

    public JwtAuthenticationToken(String userId, String token) {
        super(null);
        this.userId = Long.valueOf(userId);
        this.token = token;
    }

    @Override
    public String getCredentials() {
        return token;
    }

    @Override
    public Long getPrincipal() {
        return userId;
    }
}
