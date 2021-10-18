package com.jerry.savior_oauth.handlers;

import com.jerry.redis.utils.RedisHelper;
import com.jerry.savior_common.response.CommonResponse;
import com.jerry.savior_oauth.pojo.BO.UserInfoBO;
import com.jerry.savior_oauth.utils.RedisKeyUtil;
import com.jerry.savior_web.utils.JsonResponseWritingHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 22454
 * 注销登录处理类
 */
@Slf4j
@Component
public class AuthLogoutHandler implements LogoutSuccessHandler {

    private final RedisHelper redisHelper;

    public AuthLogoutHandler(RedisHelper redisHelper) {
        this.redisHelper = redisHelper;
    }

    @SneakyThrows
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication auth) {
        UserInfoBO principal = (UserInfoBO) auth.getPrincipal();
        String userId = String.valueOf(principal.getId());
        log.info("ID: {} 的用户注销成功", userId);
        redisHelper.getRedisTemplate().delete(RedisKeyUtil.buildAuthTokenKey(principal.getId()));
        CommonResponse<Void> successResponse = CommonResponse.build(null, "注销成功");
        JsonResponseWritingHelper.writeJsonResponse(response, successResponse);
    }
}
