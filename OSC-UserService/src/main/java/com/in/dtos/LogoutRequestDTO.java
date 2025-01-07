package com.in.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogoutRequestDTO {
    @NotBlank(message = "userId cannot be blank")
    private String userId;
    @NotBlank(message = "sessionId cannot be blank")
    private String sessionId;
}
