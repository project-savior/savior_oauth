package com.jerry.savior_oauth.handlers;

import com.jerry.redis.utils.RedisHelper;
import com.jerry.savior_common.response.CommonResponse;
import com.jerry.savior_common.util.ObjectMapperHelper;
import com.jerry.savior_common.util.TokenHelper;
import com.jerry.savior_oauth.constants.AccessibleConstants;
import com.jerry.savior_oauth.constants.RedisConstants;
import com.jerry.savior_oauth.pojo.VO.LoginSuccessVO;
import com.jerry.savior_oauth.pojo.VO.UserVO;
import com.jerry.savior_oauth.utils.RedisKeyUtil;
import com.jerry.savior_web.utils.JsonResponseWritingHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Set;

/**
 * @author 22454
 * 认证登录成功处理类
 */
@Slf4j
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    private final TokenHelper tokenHelper;
    public final RedisHelper redisHelper;

    public AuthSuccessHandler(TokenHelper tokenHelper,
                              RedisHelper redisHelper) {
        this.tokenHelper = tokenHelper;
        this.redisHelper = redisHelper;
    }

    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication auth) {
        UserVO principal = (UserVO) auth.getPrincipal();
        String userId = String.valueOf(principal.getId());
        log.info("ID: {} 的用户认证成功", userId);
        String token = tokenHelper.buildToken(userId);
        redisHelper.opsForString().set(
                RedisKeyUtil.buildAuthTokenKey(principal.getId()),
                token,
                Duration.ofSeconds(tokenHelper.getTokenExpire()));
        Set<Long> permissionSet = principal.getPermissionSet();
        Long longUserId = principal.getId();
        for (Long permission : permissionSet) {
            String accessible = RedisKeyUtil.buildAccessible(longUserId, permission);
            redisHelper.opsForHash().hSet(
                    RedisConstants.HashBigKeyConstants.ACCESSIBLE,
                    accessible,
                    AccessibleConstants.YES);
        }
        LoginSuccessVO loginSuccessVO = new LoginSuccessVO();
        loginSuccessVO.setToken(token);
        CommonResponse<LoginSuccessVO> loginSuccess = CommonResponse.build(loginSuccessVO, "登录成功");
        JsonResponseWritingHelper.writeJsonResponse(response, loginSuccess);
    }
}
