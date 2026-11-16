package com.example.identity.oktaidentityservice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorModel {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "This is the numeric code describing the overall nature of the error")
    private Integer code;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Message that can be displayed to a user")
    private String userMessage;

    @Schema(description = "Error message. This may be a technical in nature, and is not necessarily intended for display to a user")
    private String message;

    @Schema(description = "Further information related to the error.")
    private String additionalInfo;
}
