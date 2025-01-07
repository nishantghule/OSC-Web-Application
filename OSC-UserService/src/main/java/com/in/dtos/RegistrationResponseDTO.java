package com.in.dtos;

import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponseDTO {
    private String userId;
    private String name;
    private String email;
    private LocalDate DOB;
    private String contact;
    private String password;
}