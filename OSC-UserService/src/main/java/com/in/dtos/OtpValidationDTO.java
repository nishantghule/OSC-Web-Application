package com.in.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpValidationDTO {
    @NotBlank(message = "userId cannot be blank")
    private String userId;

    @NotBlank(message = "OTP cannot be blank")
    @JsonProperty("OTP")
    private String OTP;

}
