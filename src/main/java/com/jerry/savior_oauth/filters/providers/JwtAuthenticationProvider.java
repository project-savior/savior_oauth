package com.jerry.savior_oauth.filters.providers;

import com.jerry.redis.utils.RedisHelper;
import com.jerry.savior_oauth.filters.tokens.JwtAuthenticationToken;
import com.jerry.savior_oauth.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @author 22454
 */
@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final RedisHelper redisHelper;

    public JwtAuthenticationProvider(RedisHelper redisHelper) {
        this.redisHelper = redisHelper;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        Long userId = jwtAuthenticationToken.getPrincipal();
        String token = jwtAuthenticationToken.getCredentials();
        String tokenKey = RedisKeyUtil.buildAuthTokenKey(userId);
        String tokenInRedis = redisHelper.opsForString().get(tokenKey);
        if (tokenInRedis == null || !tokenInRedis.equals(token)) {
            log.info("token expired");
            throw new BadCredentialsException("凭证已过期");
        }
        log.info("token 正常，可通行");
        return jwtAuthenticationToken;

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return JwtAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
