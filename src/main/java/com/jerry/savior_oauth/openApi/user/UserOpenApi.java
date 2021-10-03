package com.jerry.savior_oauth.openApi.user;

import com.jerry.savior_common.response.CommonResponse;
import com.jerry.savior_oauth.openApi.user.fallback.UserOpenApiFallback;
import com.jerry.savior_oauth.pojo.BO.UserInfoBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 22454
 */
@FeignClient(name = "savior-user",
        path = "/user",
        fallback = UserOpenApiFallback.class)
public interface UserOpenApi {

    /**
     * 根据手机号获取用户信息
     *
     * @param phone 手机号
     * @return 用户信息
     */
    @GetMapping("/getUserInfoByPhone")
    CommonResponse<UserInfoBO> getUserInfoByPhone(String phone);
}
