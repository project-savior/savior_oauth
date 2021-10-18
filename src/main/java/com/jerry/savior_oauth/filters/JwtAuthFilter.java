package com.jerry.savior_oauth.filters;

import com.jerry.savior_common.util.TokenHelper;
import com.jerry.savior_oauth.filters.tokens.DynamicAuthenticationToken;
import com.jerry.savior_oauth.utils.RedisKeyUtil;
import com.jerry.savior_web.utils.SpringContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 22454
 */
@Slf4j
public class JwtAuthFilter extends BasicAuthenticationFilter {
    TokenHelper tokenHelper = SpringContextHelper.getBean(TokenHelper.class);
    RedisTemplate redisTemplate = SpringContextHelper.getBean(RedisTemplate.class);

    public JwtAuthFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = tokenHelper.getTokenFromRequest(request);
        if (StringUtils.isNotBlank(token) && !tokenHelper.hasExpired(token)) {
            String userId = tokenHelper.getSubject(token);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (userId != null && authentication == null) {
                String tokenKey = RedisKeyUtil.buildAuthTokenKey(Long.valueOf(userId));
                String tokenInRedis = (String) redisTemplate.opsForValue().get(tokenKey);
                if (tokenInRedis != null && tokenInRedis.equals(token)) {
                    log.info("token 正常，可通行");
                    DynamicAuthenticationToken dynamicAuthenticationToken = new DynamicAuthenticationToken(userId, token, true);
                    onSuccessfulAuthentication(request, response, dynamicAuthenticationToken);
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        super.onSuccessfulAuthentication(request, response, authResult);
        SecurityContextHolder.getContext().setAuthentication(authResult);
    }
}
