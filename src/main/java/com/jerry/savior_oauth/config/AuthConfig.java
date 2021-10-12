package com.jerry.savior_oauth.config;

import com.jerry.savior_oauth.filters.DynamicAuthFilter;
import com.jerry.savior_oauth.filters.JwtAuthFilter;
import com.jerry.savior_oauth.filters.providers.DynamicAuthenticationProvider;
import com.jerry.savior_oauth.handlers.AuthFailureHandler;
import com.jerry.savior_oauth.handlers.AuthSuccessHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @author 22454
 */
@Component
public class AuthConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final AuthSuccessHandler authSuccessHandler;
    private final AuthFailureHandler authFailureHandler;
    private final DynamicAuthenticationProvider dynamicAuthenticationProvider;

    public AuthConfig(AuthSuccessHandler authSuccessHandler,
                      AuthFailureHandler authFailureHandler,
                      DynamicAuthenticationProvider dynamicAuthenticationProvider) {
        this.authSuccessHandler = authSuccessHandler;
        this.authFailureHandler = authFailureHandler;
        this.dynamicAuthenticationProvider = dynamicAuthenticationProvider;
    }

    @Override
    public void configure(HttpSecurity builder) {
        DynamicAuthFilter dynamicAuthFilter = new DynamicAuthFilter();
        AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
        dynamicAuthFilter.setAuthenticationManager(authenticationManager);
        dynamicAuthFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        dynamicAuthFilter.setAuthenticationFailureHandler(authFailureHandler);

        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(authenticationManager);
        builder.authenticationProvider(dynamicAuthenticationProvider)
                .addFilterBefore(dynamicAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, DynamicAuthFilter.class);
    }
}
