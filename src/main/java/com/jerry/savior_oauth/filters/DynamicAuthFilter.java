package com.jerry.savior_oauth.filters;

import com.jerry.savior_common.error.BusinessException;
import com.jerry.savior_oauth.error.EnumAuthException;
import com.jerry.savior_oauth.filters.tokens.DynamicAuthenticationToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 22454
 */
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
            throws AuthenticationException, IOException, ServletException {
        String phone = obtainPhone(request);
        String dynamic = obtainDynamic(request);
        if (StringUtils.isBlank(phone)) {
            throw new BusinessException(EnumAuthException.MALFORMED_PHONE_NUMBER);
        }
        if (StringUtils.isBlank(dynamic)) {
            throw new BusinessException(EnumAuthException.VERIFICATION_CODE_ERROR);
        }
        DynamicAuthenticationToken authenticationToken = new DynamicAuthenticationToken(phone, dynamic);
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
