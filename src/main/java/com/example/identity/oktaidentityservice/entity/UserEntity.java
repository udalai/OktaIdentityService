package com.example.identity.oktaidentityservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class UserEntity {

    @Id
    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "OKTA_ID")
    private String oktaId;

    @Column(name = "CUSTOMER_NUMBER")
    private String customerNumber;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_TIME")
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_TIME")
    private Date modifiedTime;

}
