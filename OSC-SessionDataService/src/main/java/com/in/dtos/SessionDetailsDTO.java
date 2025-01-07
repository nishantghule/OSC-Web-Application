package com.in.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionDetailsDTO {
    private String sessionId;
    private String userId;
    private String loginDevice;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
}
