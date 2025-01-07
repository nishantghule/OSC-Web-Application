package com.in.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpDataDTO {
    private String userId;
    private String OTP;
    private String email;
    private int failedAttempts;
}
