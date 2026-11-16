package com.example.identity.oktaidentityservice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDetails {

    private String errorCode;

    private String errorSummary;

    private String errorLink;

    private String errorId;
}
