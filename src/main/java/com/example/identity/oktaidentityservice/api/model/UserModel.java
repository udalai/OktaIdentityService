package com.example.identity.oktaidentityservice.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserModel {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "First Name")
    @NotEmpty(message = "firstName is required")
    private String firstName;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Last Name")
    @NotEmpty(message = "lastName is required")
    private String lastName;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Email Address")
    @Email(message = "email format is invalid")
    @NotEmpty(message = "email is required")
    private String email;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Unique userName")
    @NotEmpty(message = "userName is required")
    private String userName;

}
