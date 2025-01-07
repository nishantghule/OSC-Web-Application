package com.in.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserDTO {
    private String userId;
    private String name;
    private String email;
    private LocalDate DOB;
    private String contact;
    private String password;
    }
