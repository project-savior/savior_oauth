package com.jerry.savior_oauth.error;

import com.jerry.savior_common.asserts.BusinessExceptionAssert;

/**
 * @author 22454
 */

public enum EnumAuthException implements BusinessExceptionAssert {
    // 未登录
    NOT_LOGGED_IN(100001, "您当前未登录，请登录后重试"),
    // 权限不足
    INSUFFICIENT_PERMISSIONS(100002, "权限不足，禁止访问"),
    // 认证失败
    AUTHENTICATION_FAILED(100003, "用户信息认证失败"),
    // 手机号不得为空
    MALFORMED_PHONE_NUMBER(100004,"手机号码格式错误，请重新输入"),
    // 验证码错误
    VERIFICATION_CODE_ERROR(100005,"验证码错误")
    ;
    private final int code;
    private final String message;

    EnumAuthException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
