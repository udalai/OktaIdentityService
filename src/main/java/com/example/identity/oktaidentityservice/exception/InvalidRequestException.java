package com.example.identity.oktaidentityservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;

@Getter
@AllArgsConstructor
public class InvalidRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 817387428738478374L;

    private final String userMessage;


}
