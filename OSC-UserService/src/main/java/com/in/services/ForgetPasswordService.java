package com.in.services;

import com.in.dtos.EmailDTO;
import com.in.dtos.ResponseCode;

public interface ForgetPasswordService {
    ResponseCode forgetPassword(EmailDTO emailDTO);
}
