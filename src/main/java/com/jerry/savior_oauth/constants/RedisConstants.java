package com.jerry.savior_oauth.constants;

/**
 * @author 22454
 */
public interface RedisConstants {
    String DYNAMIC_CODE_KEY = "Dynamic-Auth= %s";
    String AUTH_TOKEN_KEY = "Auth-Token= %s";

    String CAN_ACCESSIBLE = "[ %s ==> %s ]";

    interface HashBigKeyConstants {
        String ACCESSIBLE = "Accessible";
    }
}
