package com.jerry.savior_oauth.handlers;

import com.jerry.savior_common.response.CommonResponse;
import com.jerry.savior_common.util.ObjectMapperHelper;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author 22454
 * 注销登录处理类
 */
@Component
public class AuthLogoutHandler implements LogoutSuccessHandler {
    private final ObjectMapperHelper objectMapperHelper;

    public AuthLogoutHandler(ObjectMapperHelper objectMapperHelper) {
        this.objectMapperHelper = objectMapperHelper;
    }

    @SneakyThrows
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication auth) {
        //TODO 删除 redis 中的 token
        CommonResponse<Void> successResponse = CommonResponse.build(null, "注销成功");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        writer.write(objectMapperHelper.toJson(successResponse));
        writer.flush();
    }
}
