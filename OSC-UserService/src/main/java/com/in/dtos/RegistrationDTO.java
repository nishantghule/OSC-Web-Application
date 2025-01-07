package com.in.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {
    @NotBlank(message = "name cannot be blank")
    @Pattern(regexp = "^[A-Za-z]+( [A-Za-z]+){1,2}$", message = "Enter correct Name")
    private String name;

    @NotBlank(message = "email cannot be blank")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Enter correct email")
    private String email;

    @NotNull(message = "Date of birth cannot be null/empty")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate DOB;

    @NotBlank(message = "Mobile number cannot be empty")
    @Pattern(regexp = "^\\d{10}$", message = "Enter 10 digit mobile number")
    private String contact;
}
