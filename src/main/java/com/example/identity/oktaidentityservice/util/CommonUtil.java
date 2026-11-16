package com.example.identity.oktaidentityservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtil {
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
