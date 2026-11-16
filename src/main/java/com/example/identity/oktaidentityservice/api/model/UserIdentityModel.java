package com.example.identity.oktaidentityservice.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserIdentityModel extends UserModel {

    @Schema(description = "OKTA ID of the User")
    @JsonProperty("oktaId")
    private String oktaId;

    @Schema(description = "Created Timestamp of the User")
    @JsonProperty("createdTime")
    private Date createdTime;

    @Schema(description = "Modified Timestamp of the User")
    @JsonProperty("modifiedTime")
    private Date modifiedTime;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Created by User details")
    @NotNull(message = "createdBy is required")
    @Valid
    @JsonProperty("createdBy")
    private UserModel createdBy;

    @Schema(description = "modifiedBy UserName")
    @JsonProperty("modifiedBy")
    private String modifiedBy;


    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Customer Number")
    @NotNull(message = "customerNumber is required")
    @JsonProperty("customerNumber")
    private String customerNumber;


    @Builder
    public UserIdentityModel(String firstname, String lastName, String email, String userName,
                             String oktaId, Date createdTime, Date modifiedTime, UserModel createdBy, String modifiedBy, String customerNumber) {
        super(firstname, lastName, email, userName);
        this.oktaId = oktaId;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.customerNumber = customerNumber;
    }
}
