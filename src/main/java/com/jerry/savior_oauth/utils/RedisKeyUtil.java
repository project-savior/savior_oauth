package com.jerry.savior_oauth.utils;

import com.jerry.savior_oauth.constants.RedisConstants;

/**
 * @author 22454
 */
public class RedisKeyUtil {
    public static String buildDynamicAuthKey(String mobile) {
        return String.format(RedisConstants.DYNAMIC_CODE_KEY, mobile);
    }

    public static String buildAuthTokenKey(Long userId) {
        return String.format(RedisConstants.AUTH_TOKEN_KEY, userId);
    }

    public static String buildAccessible(Long userId, Long permissionId) {
        return String.format(RedisConstants.CAN_ACCESSIBLE, userId, permissionId);
    }
}
