package com.jerry.savior_oauth.handlers;

import com.jerry.savior_common.response.CommonResponse;
import com.jerry.savior_common.util.ObjectMapperHelper;
import com.jerry.savior_oauth.error.EnumAuthException;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author 22454
 */
@Component
public class AccessDenyHandler implements AccessDeniedHandler {
    private final ObjectMapperHelper objectMapperHelper;

    public AccessDenyHandler(ObjectMapperHelper objectMapperHelper) {
        this.objectMapperHelper = objectMapperHelper;
    }

    @SneakyThrows
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        CommonResponse<Object> denyResponse = CommonResponse.build(
                EnumAuthException.INSUFFICIENT_PERMISSIONS.getCode(),
                null,
                EnumAuthException.INSUFFICIENT_PERMISSIONS.getMessage());
        PrintWriter writer = response.getWriter();
        writer.write(objectMapperHelper.toJson(denyResponse));
        writer.flush();
    }
}
