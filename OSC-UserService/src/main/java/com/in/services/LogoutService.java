package com.in.services;

import com.in.dtos.LogoutRequestDTO;
import com.in.dtos.ResponseCode;

public interface LogoutService {
     ResponseCode logoutRequest(LogoutRequestDTO logoutRequestDTO);
}
