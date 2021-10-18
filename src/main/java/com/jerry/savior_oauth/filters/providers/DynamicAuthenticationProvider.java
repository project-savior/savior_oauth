package com.jerry.savior_oauth.filters.providers;

import com.jerry.redis.utils.RedisHelper;
import com.jerry.savior_common.response.CommonResponse;
import com.jerry.savior_oauth.filters.tokens.DynamicAuthenticationToken;
import com.jerry.savior_oauth.openApi.user.UserOpenApi;
import com.jerry.savior_oauth.pojo.BO.UserInfoBO;
import com.jerry.savior_oauth.pojo.VO.UserVO;
import com.jerry.savior_oauth.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @author 22454
 */
@Slf4j
@Component
public class DynamicAuthenticationProvider implements AuthenticationProvider {
    private final UserOpenApi userOpenApi;
    private final RedisHelper redisHelper;

    public DynamicAuthenticationProvider(UserOpenApi userOpenApi,
                                         RedisHelper redisHelper) {
        this.userOpenApi = userOpenApi;

        this.redisHelper = redisHelper;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        DynamicAuthenticationToken dynamicAuthenticationToken = (DynamicAuthenticationToken) authentication;
        if (dynamicAuthenticationToken.isAuthenticated()) {
            return authentication;
        }
        // 从 DynamicAuthFilter 过来之后
        // 检查手机号、验证码有效性
        additionalAuthenticationChecks(dynamicAuthenticationToken);
        // 验证通过后
        // 查询用户信息
        String phone = dynamicAuthenticationToken.getPrincipal().toString();
        CommonResponse<UserInfoBO> userInfoByPhone = userOpenApi.getUserInfoByPhone(phone);
        if (!userInfoByPhone.isSuccess()) {
            throw new AuthenticationServiceException(userInfoByPhone.getMessage());
        }
        UserInfoBO userInfoBO = userInfoByPhone.getData();
        UserVO userVO = UserVO.createUserVO(userInfoBO);
        // 创建auth对象
        DynamicAuthenticationToken result = new DynamicAuthenticationToken(userVO, null, userVO.getPermissionSet(), true);
        result.setDetails(authentication.getDetails());
        return result;
    }

    protected void additionalAuthenticationChecks(DynamicAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException("验证码不能为空");
        }
        String mobile = authentication.getPrincipal().toString();
        String dynamicCode = authentication.getCredentials().toString();
        String dynamicAuthKey = RedisKeyUtil.buildDynamicAuthKey(mobile);
        String dynamicInRedis = redisHelper.getRedisTemplate().opsForValue().get(dynamicAuthKey);
        // 如果输入的验证码为空 / redis中验证码为空 / 输入的验证码与redis中的验证码不一致
        if (StringUtils.isBlank(dynamicCode) ||
                StringUtils.isBlank(dynamicInRedis) ||
                !dynamicCode.equalsIgnoreCase(dynamicInRedis)) {
            throw new BadCredentialsException("验证码错误");
        }
        // 验证通过，清除验证码
        redisHelper.getRedisTemplate().delete(dynamicAuthKey);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (DynamicAuthenticationToken.class.isAssignableFrom(aClass));
    }
}
