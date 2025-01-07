/*
package com.in.controllers;

import com.in.dtos.ForgetPasswordRequestDTO;
import com.in.dtos.OtpValidationDTO;
import com.in.dtos.ResponseCode;
import com.in.mapper.StatusCodeMapper;
import com.in.services.OtpValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class OtpValidationController {
    private final Logger log = LogManager.getLogger(OtpValidationController.class);
    private final OtpValidationService otpValidationService;

    @PostMapping("/validateOtp")
    public ResponseEntity<ResponseCode> validateOtp(@Valid @RequestBody OtpValidationDTO request){
        log.info("Request received to validate otp on registration: {}", request);
        ResponseCode response = otpValidationService.validateOtpOnRegistration(request);
        return ResponseEntity.status(StatusCodeMapper.mapStatusCode(response.getCode())).body(response);
    }
    @PostMapping("/validateOTPForForgotPassword")
    public ResponseEntity<ResponseCode> validateOtpOnForgetPassword(@Valid @RequestBody ForgetPasswordRequestDTO request){
        log.info("Request received to validate otp on forget password: {}", request);
        ResponseCode response = otpValidationService.validateOtpOnForgetPassword(request);
        return ResponseEntity.status(StatusCodeMapper.mapStatusCode(response.getCode())).body(response);
    }

}
*/
