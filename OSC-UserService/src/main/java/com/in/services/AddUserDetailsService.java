package com.in.services;

import com.in.dtos.ResponseCode;
import com.in.dtos.SetPasswordDTO;

public interface AddUserDetailsService {
    ResponseCode addUserDetails(SetPasswordDTO setPasswordDTO);
}
