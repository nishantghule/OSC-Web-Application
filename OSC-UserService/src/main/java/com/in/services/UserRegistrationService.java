package com.in.services;

import com.in.dtos.RegistrationDTO;
import com.in.dtos.Response;

public interface UserRegistrationService {
    Response registerUser(RegistrationDTO registrationDTO);
}
