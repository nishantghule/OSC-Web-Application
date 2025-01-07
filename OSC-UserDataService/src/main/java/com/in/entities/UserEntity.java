package com.in.entities;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
@Data
@Entity
@Table(name = "UserInfo")
public class UserEntity {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_email")
    private String email;

    @Column(name = "date_of_birth")
    private LocalDate DOB;

    @Column(name = "mobile_no")
    private String contact;

    @Column(name = "password")
    private String password;
}