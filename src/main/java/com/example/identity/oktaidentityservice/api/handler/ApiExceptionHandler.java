package com.example.identity.oktaidentityservice.api.handler;

import com.example.identity.oktaidentityservice.api.model.ErrorDetails;
import com.example.identity.oktaidentityservice.api.model.ErrorModel;
import com.example.identity.oktaidentityservice.exception.DataNotFoundException;
import com.example.identity.oktaidentityservice.exception.InvalidRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okta.sdk.resource.client.ApiException;
import com.okta.sdk.resource.client.StringUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.config.ConfigDataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorModel> handleApiException(ApiException apiException) throws JsonProcessingException {
        log.error("ApiExceptionHandler -> handleApiException: ", apiException);
        return ResponseEntity
                .status(apiException.getCode())
                .body(buildErrorModel(apiException));
    }

    private ErrorModel buildErrorModel(ApiException apiException) throws JsonProcessingException{
        ErrorDetails errorDetails = new ObjectMapper().readValue(apiException.getResponseBody(), ErrorDetails.class);

        log.info("ErrorDetails: {}", errorDetails);
        return ErrorModel.builder()
                .message(errorDetails.getErrorCode())
                .userMessage(errorDetails.getErrorSummary())
                .additionalInfo(errorDetails.getErrorId())
                .build();
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorModel> handleDataNotFoundException(DataNotFoundException ex) {
        log.error("ApiExceptionHandler -> handleDataNotFoundException: {}", ex.getUserMessage());

        var errorModel = ErrorModel.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .userMessage(ex.getUserMessage())
                .additionalInfo(StringUtils.EMPTY)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorModel);
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorModel> handleInvalidRequestException(InvalidRequestException ex) {
        log.error("ApiExceptionHandler -> handleInvalidRequestException: {}", ex.getUserMessage());

        var errorModel = ErrorModel.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .userMessage(ex.getUserMessage())
                .additionalInfo(StringUtils.EMPTY)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorModel);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorModel handleAccessDeniedException(AccessDeniedException ex) {
        log.error("ApiExceptionHandler -> handleAccessDeniedException: {}", ex.getMessage());

        return ErrorModel.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .userMessage(ex.getMessage())
                .build();
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorModel handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("ApiExceptionHandler -> handleConstraintViolationException: {}", ex.getMessage());

        var errorMessage = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.joining(","));


        return ErrorModel.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .userMessage(errorMessage)
                .message("Validation eror")
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorModel> handleGenericException(Exception ex) {
        log.error("ApiExceptionHandler -> handleGenericException: ", ex);

        var errorModel = ErrorModel.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .userMessage("Unable to process the request")
                .additionalInfo(StringUtils.EMPTY)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(errorModel);
    }

}
