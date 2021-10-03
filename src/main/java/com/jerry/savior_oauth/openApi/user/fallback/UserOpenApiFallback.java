package com.jerry.savior_oauth.openApi.user.fallback;

import com.jerry.savior_common.error.BusinessException;
import com.jerry.savior_common.response.CommonResponse;
import com.jerry.savior_oauth.error.EnumAuthException;
import com.jerry.savior_oauth.openApi.user.UserOpenApi;
import com.jerry.savior_oauth.pojo.BO.UserInfoBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author 22454
 */
@Slf4j
@Component
public class UserOpenApiFallback implements UserOpenApi {

    @Override
    public CommonResponse<UserInfoBO> getUserInfoByPhone(String phone) {
        log.error("手机号为： {} 的用户信息获取失败，远程调用找不到服务Provider", phone);
        throw new BusinessException(EnumAuthException.AUTHENTICATION_FAILED);
    }
}
