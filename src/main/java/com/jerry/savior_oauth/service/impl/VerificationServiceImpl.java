package com.jerry.savior_oauth.service.impl;

import com.jerry.redis.utils.RedisHelper;
import com.jerry.savior_oauth.service.IVerificationService;
import com.jerry.savior_oauth.utils.RedisKeyUtil;
import org.springframework.stereotype.Service;

/**
 * @author 22454
 */
@Service
public class VerificationServiceImpl implements IVerificationService {
    private final RedisHelper redisHelper;

    public VerificationServiceImpl(RedisHelper redisHelper) {
        this.redisHelper = redisHelper;
    }

    @Override
    public String sendDynamic(String phone) {
        String dynamic = "1234";
        redisHelper.opsForString().set(RedisKeyUtil.buildDynamicAuthKey(phone), dynamic);
        return dynamic;
    }
}
