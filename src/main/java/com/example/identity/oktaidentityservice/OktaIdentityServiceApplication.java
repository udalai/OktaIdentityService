package com.example.identity.oktaidentityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@SuppressWarnings({"PMD.UseUtilityClass", "checkstyle:hideUtilityclassconstructor"})
@EnableFeignClients(basePackages = "com.example.identity")
public class OktaIdentityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OktaIdentityServiceApplication.class, args);
    }

}
