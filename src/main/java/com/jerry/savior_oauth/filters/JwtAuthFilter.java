package com.jerry.savior_oauth.filters;

import com.jerry.savior_common.util.TokenHelper;
import com.jerry.savior_oauth.filters.tokens.JwtAuthenticationToken;
import com.jerry.savior_web.utils.SpringContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
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

    public JwtAuthFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = tokenHelper.getTokenFromRequest(request);
        if (StringUtils.isNotBlank(token)) {
            String userId = tokenHelper.getSubject(token);
            if (userId != null) {
                JwtAuthenticationToken dynamicAuthenticationToken = new JwtAuthenticationToken(userId, token);
                SecurityContextHolder.getContext().setAuthentication(dynamicAuthenticationToken);
            } else {
                log.info("token error");
            }

        } else {
            log.info("request don't have token");
        }

        chain.doFilter(request, response);

    }
}
