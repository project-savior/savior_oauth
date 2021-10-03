package com.jerry.savior_oauth.handlers;

import com.jerry.savior_common.response.CommonResponse;
import com.jerry.savior_common.util.ObjectMapperHelper;
import com.jerry.savior_common.util.TokenHelper;
import com.jerry.savior_oauth.pojo.BO.UserInfoBO;
import com.jerry.savior_oauth.pojo.VO.LoginSuccessVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 22454
 * 认证登录成功处理类
 */
@Slf4j
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapperHelper objectMapperHelper;
    private final TokenHelper tokenHelper;
    public static final Map<String, String> redis = new HashMap<>();

    public AuthSuccessHandler(ObjectMapperHelper objectMapperHelper,
                              TokenHelper tokenHelper) {
        this.objectMapperHelper = objectMapperHelper;
        this.tokenHelper = tokenHelper;
    }

    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication auth) {
        PrintWriter writer = response.getWriter();
        UserInfoBO principal = (UserInfoBO) auth.getPrincipal();
        String userId = String.valueOf(principal.getId());
        String token = tokenHelper.buildToken(userId);
        //TODO 存入redis
        log.info("token 存入redis");
        redis.put(userId, token);
        LoginSuccessVO loginSuccessVO = new LoginSuccessVO();
        loginSuccessVO.setToken(token);
        CommonResponse<LoginSuccessVO> loginSuccess = CommonResponse.build(loginSuccessVO, "登录成功");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        writer.write(objectMapperHelper.toJson(loginSuccess));
        writer.flush();
    }
}
