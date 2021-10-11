package com.jerry.savior_oauth.service;

/**
 * @author 22454
 */
public interface IVerificationService {
    /**
     * 发送短信验证码
     * @param phone 手机号
     * @return 验证码  TODO（生产环境不得返回）
     */
    String sendDynamic(String phone);
}
