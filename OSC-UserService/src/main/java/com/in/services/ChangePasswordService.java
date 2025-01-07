package com.in.services;

import com.in.dtos.ChangePasswordDTO;
import com.in.dtos.ResponseCode;

public interface ChangePasswordService {
    ResponseCode changePassword(ChangePasswordDTO changePasswordDTO);
}
