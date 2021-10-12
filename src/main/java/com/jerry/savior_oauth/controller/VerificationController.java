package com.jerry.savior_oauth.controller;

import com.jerry.savior_common.constants.PatternConstants;
import com.jerry.savior_common.response.CommonResponse;
import com.jerry.savior_oauth.service.IVerificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;

/**
 * @author 22454
 */
@RestController
@RequestMapping("/code")
public class VerificationController {
    private final IVerificationService verificationService;

    public VerificationController(IVerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping("/dynamic")
    public CommonResponse<String> dynamic(@RequestParam("phone")
                                        @Pattern(regexp = PatternConstants.TELEPHONE_REGEXP, message = "手机号码格式错误")
                                                String phone) {
        String dynamic = verificationService.sendDynamic(phone);

        return CommonResponse.build(dynamic, "验证码发送成功");
    }
}
