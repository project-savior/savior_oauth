package com.jerry.savior_oauth.handlers;

import com.jerry.savior_common.response.CommonResponse;
import com.jerry.savior_common.util.ObjectMapperHelper;
import com.jerry.savior_oauth.error.EnumAuthException;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author 22454
 * 认证异常的处理类
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapperHelper objectMapperHelper;

    public MyAuthenticationEntryPoint(ObjectMapperHelper objectMapperHelper) {
        this.objectMapperHelper = objectMapperHelper;
    }

    @SneakyThrows
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) {
        CommonResponse<Void> authErrorResponse = new CommonResponse<>();
        authErrorResponse.setCode(EnumAuthException.NOT_LOGGED_IN.getCode());
        authErrorResponse.setMessage(EnumAuthException.NOT_LOGGED_IN.getMessage());
        authErrorResponse.setData(null);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        writer.write(objectMapperHelper.toJson(authErrorResponse));
        writer.flush();
    }
}
