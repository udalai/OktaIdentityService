package com.example.identity.oktaidentityservice.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER_LOGIN")
public class UserLoginEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "OKTA_ID")
    private String oktaId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOGIN_TIME")
    private Date loginTime;

}
