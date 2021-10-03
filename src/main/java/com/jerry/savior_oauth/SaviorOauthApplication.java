package com.jerry.savior_oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 22454
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.jerry")
public class SaviorOauthApplication {


    public static void main(String[] args) {
        SpringApplication.run(SaviorOauthApplication.class, args);
    }

}
