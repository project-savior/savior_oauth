package com.jerry.savior_oauth.handlers;

import com.jerry.savior_common.response.CommonResponse;
import com.jerry.savior_common.util.ObjectMapperHelper;
import com.jerry.savior_web.utils.JsonResponseWritingHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author 22454
 * 登录认证失败的处理类
 */
@Slf4j
@Component
public class AuthFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapperHelper objectMapperHelper;

    public AuthFailureHandler(ObjectMapperHelper objectMapperHelper) {
        this.objectMapperHelper = objectMapperHelper;
    }

    @SneakyThrows
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) {
        log.info("认证失败");
        CommonResponse<Object> failResponse = CommonResponse.build(null, e.getMessage());
        JsonResponseWritingHelper.writeJsonResponse(response, failResponse);
    }
}
