package com.jerry.savior_oauth.handlers;

import com.jerry.savior_common.response.CommonResponse;
import com.jerry.savior_oauth.error.EnumAuthException;
import com.jerry.savior_web.utils.JsonResponseWritingHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 22454
 * 认证异常的处理类
 */
@Slf4j
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @SneakyThrows
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) {
        e.printStackTrace();
        log.info("认证异常");
        CommonResponse<Void> authErrorResponse = new CommonResponse<>();
        authErrorResponse.setCode(EnumAuthException.NOT_LOGGED_IN.getCode());

        authErrorResponse.setMessage(e.getMessage());
        authErrorResponse.setData(null);
        JsonResponseWritingHelper.writeJsonResponse(response, authErrorResponse);
    }
}
