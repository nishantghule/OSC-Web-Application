package com.in.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {
    @NotEmpty(message = "Email cannot be empty")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Enter correct Email")
    private String email;
}