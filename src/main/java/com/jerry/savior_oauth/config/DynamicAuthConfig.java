package com.jerry.savior_oauth.config;

import com.jerry.savior_oauth.filters.DynamicAuthFilter;
import com.jerry.savior_oauth.filters.providers.DynamicAuthenticationProvider;
import com.jerry.savior_oauth.handlers.AuthFailureHandler;
import com.jerry.savior_oauth.handlers.AuthSuccessHandler;
import com.jerry.savior_oauth.openApi.user.UserOpenApi;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class DynamicAuthConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final UserOpenApi userOpenApi;
    private final AuthSuccessHandler authSuccessHandler;
    private final AuthFailureHandler authFailureHandler;

    public DynamicAuthConfig(UserOpenApi userOpenApi,
                             AuthSuccessHandler authSuccessHandler,
                             AuthFailureHandler authFailureHandler) {
        this.userOpenApi = userOpenApi;
        this.authSuccessHandler = authSuccessHandler;
        this.authFailureHandler = authFailureHandler;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        DynamicAuthFilter dynamicAuthFilter = new DynamicAuthFilter();
        dynamicAuthFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        dynamicAuthFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        dynamicAuthFilter.setAuthenticationFailureHandler(authFailureHandler);

        DynamicAuthenticationProvider dynamicAuthenticationProvider = new DynamicAuthenticationProvider();
        dynamicAuthenticationProvider.setUserOpenApi(userOpenApi);

        builder.authenticationProvider(dynamicAuthenticationProvider)
                .addFilterBefore(dynamicAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
