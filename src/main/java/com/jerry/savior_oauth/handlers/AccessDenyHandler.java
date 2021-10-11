package com.jerry.savior_oauth.handlers;

import com.jerry.savior_common.response.CommonResponse;
import com.jerry.savior_oauth.error.EnumAuthException;
import com.jerry.savior_web.utils.JsonResponseWritingHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 22454
 */
@Slf4j
@Component
public class AccessDenyHandler implements AccessDeniedHandler {

    @SneakyThrows
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) {
        log.info("权限不足");
        CommonResponse<Object> denyResponse = CommonResponse.build(
                EnumAuthException.INSUFFICIENT_PERMISSIONS.getCode(),
                null,
                EnumAuthException.INSUFFICIENT_PERMISSIONS.getMessage());
        JsonResponseWritingHelper.writeJsonResponse(response, denyResponse);
    }
}
