package com.jerry.savior_oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 22454
 */
@RestController
@RequestMapping("/oauth")
public class OauthController {
    @GetMapping("/authentication")
    public void authentication(@RequestParam String token) {
    }
}
