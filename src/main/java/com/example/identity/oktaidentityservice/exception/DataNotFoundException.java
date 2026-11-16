package com.example.identity.oktaidentityservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serial;

@Getter
@AllArgsConstructor
public class DataNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 97387428738478374L;

    private final String userMessage;


}
