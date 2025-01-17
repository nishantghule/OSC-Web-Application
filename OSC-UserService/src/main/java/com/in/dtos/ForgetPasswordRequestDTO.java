package com.in.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgetPasswordRequestDTO {
    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Enter correct email")
    private String email;
    @NotBlank(message = "Otp cannot be blank")
    @JsonProperty("OTP")
    private String OTP;
}
