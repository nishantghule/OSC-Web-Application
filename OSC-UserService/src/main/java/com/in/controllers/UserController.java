package com.in.controllers;

import com.in.dtos.*;
import com.in.mappers.StatusCodeMapper;
import com.in.services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);
    private final UserRegistrationService userRegistrationService;
    private final OtpValidationService otpValidationService;
    private final AddUserDetailsService addUserDetailsService;
    private final ChangePasswordService changePasswordService;
    private final ForgetPasswordService forgetPasswordService;
    private final LoginService loginService;
    private final LogoutService logoutService;

    // User Registration (Signup)
    @PostMapping("/signup")
    public ResponseEntity<?> userRegistration(@Valid @RequestBody RegistrationDTO registrationDTO) {
        log.info("Received request to create user: {}", registrationDTO);
        Response response = userRegistrationService.registerUser(registrationDTO);
        log.info("User registration response: {}", response);
        return ResponseEntity.status(StatusCodeMapper.mapStatusCode(response.getCode())).body(response);
    }

    // OTP Validation for Registration
    @PostMapping("/validateOtp")
    public ResponseEntity<ResponseCode> validateOtp(@Valid @RequestBody OtpValidationDTO request) {
        log.info("Request received to validate OTP on registration: {}", request);
        ResponseCode response = otpValidationService.validateOtpOnRegistration(request);
        log.info("OTP validation response: {}", response);
        return ResponseEntity.status(StatusCodeMapper.mapStatusCode(response.getCode())).body(response);
    }

    // OTP Validation for Forgot Password
    @PostMapping("/validateOTPForForgotPassword")
    public ResponseEntity<ResponseCode> validateOtpOnForgetPassword(@Valid @RequestBody ForgetPasswordRequestDTO request) {
        log.info("Request received to validate OTP on forget password: {}", request);
        ResponseCode response = otpValidationService.validateOtpOnForgetPassword(request);
        log.info("OTP validation for forgot password response: {}", response);
        return ResponseEntity.status(StatusCodeMapper.mapStatusCode(response.getCode())).body(response);
    }

    // Add User Details (set password and save user to database)
    @PostMapping("/addUserDetails")
    public ResponseEntity<ResponseCode> addUserDetails(@Valid @RequestBody SetPasswordDTO setPasswordDTO) {
        log.info("Request received to add user details for userID: {}", setPasswordDTO.getUserId());
        ResponseCode response = addUserDetailsService.addUserDetails(setPasswordDTO);
        log.info("Add user details response: {}", response);
        return ResponseEntity.status(StatusCodeMapper.mapStatusCode(response.getCode())).body(response);
    }

    // User Login
    @PostMapping("/login")
    public ResponseEntity<Response> loginRequest(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        log.info("Received login request for user: {}", loginRequestDTO.getUserId());
        Response response = loginService.loginRequest(loginRequestDTO);
        log.info("Login response: {}", response);
        return ResponseEntity.status(StatusCodeMapper.mapStatusCode(response.getCode())).body(response);
    }

    // User Logout
    @PostMapping("/logout")
    public ResponseEntity<ResponseCode> logoutRequest(@Valid @RequestBody LogoutRequestDTO logoutRequestDTO) {
        log.info("Received logout request for user with user: {}", logoutRequestDTO.getUserId());
        ResponseCode response = logoutService.logoutRequest(logoutRequestDTO);
        log.info("Logout response: {}", response);
        return ResponseEntity.status(StatusCodeMapper.mapStatusCode(response.getCode())).body(response);
    }

    // Forgot Password (to send reset password email with otp)
    @PostMapping("/forgotPassword")
    public ResponseEntity<ResponseCode> forgetPassword(@RequestBody EmailDTO email) {
        log.info("Received request to initiate forgot password for email: {}", email.getEmail());
        ResponseCode response = forgetPasswordService.forgetPassword(email);
        log.info("Forgot password response: {}", response);
        return ResponseEntity.status(StatusCodeMapper.mapStatusCode(response.getCode())).body(response);
    }

    // Change Password
    @PostMapping("/changePassword")
    public ResponseEntity<ResponseCode> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        log.info("Request received to change password for user with email: {}", changePasswordDTO.getEmail());
        ResponseCode response = changePasswordService.changePassword(changePasswordDTO);
        log.info("Change password response: {}", response);
        return ResponseEntity.status(StatusCodeMapper.mapStatusCode(response.getCode())).body(response);
    }
}
