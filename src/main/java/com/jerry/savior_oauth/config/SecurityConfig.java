package com.jerry.savior_oauth.config;

import com.jerry.savior_oauth.filters.MyPermissionEvaluator;
import com.jerry.savior_oauth.handlers.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

/**
 * @author 22454
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 认证失败处理器
     */
    private final AuthFailureHandler authFailureHandler;
    /**
     * 认证成功处理器
     */
    private final AuthSuccessHandler authSuccessHandler;
    /**
     * 注销成功处理器
     */
    private final AuthLogoutHandler authLogoutHandler;
    /**
     * 认证异常处理器
     */
    private final MyAuthenticationEntryPoint myAuthenticationEntryPoint;
    /**
     * 权限不足处理器
     */
    private final AccessDenyHandler accessDenyHandler;

    /**
     * 权限评估器
     */
    private final MyPermissionEvaluator permissionEvaluator;

    /**
     * 动态码鉴权配置
     */
    private final AuthConfig authConfig;

    public SecurityConfig(AuthFailureHandler authFailureHandler,
                          AuthSuccessHandler authSuccessHandler,
                          AuthLogoutHandler authLogoutHandler,
                          MyAuthenticationEntryPoint myAuthenticationEntryPoint,
                          AccessDenyHandler accessDenyHandler,
                          MyPermissionEvaluator permissionEvaluator,
                          AuthConfig authConfig) {
        this.authFailureHandler = authFailureHandler;
        this.authSuccessHandler = authSuccessHandler;
        this.authLogoutHandler = authLogoutHandler;
        this.myAuthenticationEntryPoint = myAuthenticationEntryPoint;
        this.accessDenyHandler = accessDenyHandler;
        this.permissionEvaluator = permissionEvaluator;
        this.authConfig = authConfig;
    }

    /**
     * 指定加密方式
     *
     * @return 编码器
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("配置登录规则");
        // 配置规则
        http.apply(authConfig).and()
                .authorizeRequests()
                // 开放登录 api
                .antMatchers(
                        "/login",
                        "/health/**",
                        "/doc.html",
                        "/swagger-resources",
                        "/webjars/**",
                        "/code/**",
                        "/api-docs",
                        "/v2/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                // 配置登录认证成功处理器
                .successHandler(authSuccessHandler)
                // 配置登录认证失败处理器
                .failureHandler(authFailureHandler)
                // 开启注销api
                .and()
                .logout()
                .permitAll()
                // 配置注销成功处理器
                .logoutSuccessHandler(authLogoutHandler)
                .and().exceptionHandling()
                // 配置权限不足处理器
                .accessDeniedHandler(accessDenyHandler)
                // 配置认证异常处理器
                .authenticationEntryPoint(myAuthenticationEntryPoint)
                .and().csrf().disable();
    }


    @Bean
    public DefaultWebSecurityExpressionHandler userSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(permissionEvaluator);
        return handler;
    }
}
