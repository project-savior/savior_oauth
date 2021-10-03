package com.jerry.savior_oauth.filters.providers;

import com.jerry.savior_common.constants.StandardResponse;
import com.jerry.savior_common.error.BusinessException;
import com.jerry.savior_common.response.CommonResponse;
import com.jerry.savior_oauth.filters.tokens.DynamicAuthenticationToken;
import com.jerry.savior_oauth.openApi.user.UserOpenApi;
import com.jerry.savior_oauth.pojo.BO.UserInfoBO;
import com.jerry.savior_oauth.pojo.VO.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author 22454
 */
public class DynamicAuthenticationProvider implements AuthenticationProvider {
    private UserOpenApi userOpenApi;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        DynamicAuthenticationToken dynamicAuthenticationToken = (DynamicAuthenticationToken) authentication;
        // 本段代码理应有Bug，注意审查
        //TODO 检验验证码
        additionalAuthenticationChecks(dynamicAuthenticationToken);
        // 查询用户信息
        String phone = dynamicAuthenticationToken.getPrincipal().toString();
        CommonResponse<UserInfoBO> userInfoByPhone = userOpenApi.getUserInfoByPhone(phone);
        if (!userInfoByPhone.isSuccess()) {
            throw new BusinessException(StandardResponse.ERROR);
        }
        UserInfoBO userInfoBO = userInfoByPhone.getData();
        UserVO userVO = UserVO.createUserVO(userInfoBO);
        // 创建auth对象
        DynamicAuthenticationToken result = new DynamicAuthenticationToken(userVO, null, userVO.getPermissionSet());
        result.setDetails(authentication.getDetails());
        return result;
    }

    protected void additionalAuthenticationChecks(DynamicAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException("验证码不能为空");
        }
        String mobile = authentication.getPrincipal().toString();
        String smsCode = authentication.getCredentials().toString();

        // TODO  从Session或者Redis中获取相应的验证码
        String smsCodeInSessionKey = "SMS_CODE_" + mobile;
        String verificationCode = "1234";

        if (StringUtils.isBlank(verificationCode) || !smsCode.equalsIgnoreCase(verificationCode)) {
            throw new BadCredentialsException("验证码错误！");
        }

        //todo  清除Session或者Redis中获取相应的验证码
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (DynamicAuthenticationToken.class.isAssignableFrom(aClass));
    }

    public UserOpenApi getUserOpenApi() {
        return userOpenApi;
    }

    public void setUserOpenApi(UserOpenApi userOpenApi) {
        this.userOpenApi = userOpenApi;
    }
}
