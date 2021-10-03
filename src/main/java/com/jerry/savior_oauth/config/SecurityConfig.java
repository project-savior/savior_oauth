package com.jerry.savior_oauth.config;

import com.jerry.savior_oauth.handlers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author 22454
 */
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
     * 动态码鉴权配置
     */
    private final DynamicAuthConfig dynamicAuthConfig;

    public SecurityConfig(AuthFailureHandler authFailureHandler,
                          AuthSuccessHandler authSuccessHandler,
                          AuthLogoutHandler authLogoutHandler,
                          MyAuthenticationEntryPoint myAuthenticationEntryPoint,
                          AccessDenyHandler accessDenyHandler,
                          DynamicAuthConfig dynamicAuthConfig) {
        this.authFailureHandler = authFailureHandler;
        this.authSuccessHandler = authSuccessHandler;
        this.authLogoutHandler = authLogoutHandler;
        this.myAuthenticationEntryPoint = myAuthenticationEntryPoint;
        this.accessDenyHandler = accessDenyHandler;
        this.dynamicAuthConfig = dynamicAuthConfig;
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
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin").password("admin").authorities("/");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭csrf
        http.cors().and().csrf().disable();
        // apply自定义配置规则
        http.apply(dynamicAuthConfig);
        // 配置规则
        http.authorizeRequests().and()
                // 开放登录 api
                .formLogin().permitAll()
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
                .authenticationEntryPoint(myAuthenticationEntryPoint);

    }
}
