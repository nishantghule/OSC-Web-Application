package com.in.services;

import com.in.dtos.LoginRequestDTO;
import com.in.dtos.Response;

public interface LoginService {
    Response loginRequest(LoginRequestDTO loginRequestDTO);
}
