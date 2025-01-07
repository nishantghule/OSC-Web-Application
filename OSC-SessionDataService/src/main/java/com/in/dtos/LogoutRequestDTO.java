package com.in.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogoutRequestDTO {
    @NotBlank(message = "userId cannot be blank")
    private String userId;
    @NotBlank(message = "sessionId cannot be blank")
    private String sessionId;
}
