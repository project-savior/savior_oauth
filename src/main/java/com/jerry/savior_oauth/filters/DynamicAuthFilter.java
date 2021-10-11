package com.jerry.savior_oauth.filters;

import com.jerry.savior_oauth.error.EnumAuthException;
import com.jerry.savior_oauth.filters.tokens.DynamicAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 22454
 */
@Slf4j
public class DynamicAuthFilter extends AbstractAuthenticationProcessingFilter {
    private static final String DEFAULT_FILTER_PROCESSES_URL = "/login/dynamic";
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(DEFAULT_FILTER_PROCESSES_URL);
    private static final String PARAM_PHONE = "phone";
    private static final String PARAM_DYNAMIC = "dynamic";


    public DynamicAuthFilter() {
        super(DEFAULT_FILTER_PROCESSES_URL);
    }

    public DynamicAuthFilter(Integer placeholder) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {

        log.info("登录验证");
        // 提取手机号
        String phone = obtainPhone(request);
        // 提取验证码
        String dynamic = obtainDynamic(request);

        // 手机号为空
        if (StringUtils.isBlank(phone)) {
            throw new UsernameNotFoundException(EnumAuthException.MALFORMED_PHONE_NUMBER.getMessage());
        }

        // 验证码为空
        if (StringUtils.isBlank(dynamic)) {
            throw new UsernameNotFoundException(EnumAuthException.VERIFICATION_CODE_ERROR.getMessage());
        }
        // 通过provider认证token
        DynamicAuthenticationToken authenticationToken = new DynamicAuthenticationToken(phone, dynamic,false);
        setDetails(request, authenticationToken);
        return this.getAuthenticationManager().authenticate(authenticationToken);


    }

    private String obtainPhone(HttpServletRequest request) {
        return request.getParameter(PARAM_PHONE);
    }

    private String obtainDynamic(HttpServletRequest request) {
        return request.getParameter(PARAM_DYNAMIC);
    }

    protected void setDetails(HttpServletRequest request, DynamicAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

}
