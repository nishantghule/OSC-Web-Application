package com.in.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor
public class CreateSessionDTO {
    private String SessionId;
    private String userId;
    private String loginDevice;
}
