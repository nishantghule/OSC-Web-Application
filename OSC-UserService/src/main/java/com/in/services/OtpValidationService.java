package com.in.services;

import com.in.dtos.ForgetPasswordRequestDTO;
import com.in.dtos.OtpValidationDTO;
import com.in.dtos.ResponseCode;

public interface OtpValidationService {
    ResponseCode validateOtpOnRegistration(OtpValidationDTO otpValidationDTO);
    ResponseCode validateOtpOnForgetPassword(ForgetPasswordRequestDTO forgetPasswordRequestDTO);
}

