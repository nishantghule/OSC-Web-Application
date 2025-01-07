package com.in.mappers;

import com.in.dtos.ChangePasswordDTO;
import com.in.dtos.OtpDataDTO;
import com.in.dtos.RegistrationDTO;
import com.in.dtos.SetPasswordDTO;
import com.in.kafka.avro.Otp;
import com.in.kafka.avro.User;
import com.in.proto.user.PasswordUpdateRequest;
import com.in.proto.user.UserCredentialRequest;
import com.in.proto.user.UserEmailRequest;
import com.in.proto.user.UserEmailResponse;
import com.in.proto.user.UserRegistrationRequest;
import com.in.proto.user.UserRegistrationResponse;

import java.time.format.DateTimeFormatter;

public abstract class UserMapper {
    public static final String REGISTRATION = "REGISTRATION";
    public static final String RESET_PASSWORD = "RESET_PASSWORD";

    public static boolean responseToBooleanValue(UserEmailResponse emailVerification) {
        return emailVerification.getExists();
    }

    public static User dtoToAvro(RegistrationDTO dto) {
        // Convert dateOfBirth from LocalDate to string in "yyyy-MM-dd" format
        String dateOfBirthString = dto.getDOB().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        User avroUser = new User();
        avroUser.setName(dto.getName());
        avroUser.setEmail(dto.getEmail());
        avroUser.setDateOfBirth(dateOfBirthString);  // Set date as string
        avroUser.setContact(dto.getContact());
        return avroUser;
    }

    public static UserEmailRequest emailToRequest(String email) {
        return UserEmailRequest.newBuilder()
                .setEmail(email)
                .build();
    }

    public static Otp requestToOtpForRegistration(OtpDataDTO request) {
        return new Otp(request.getOTP(),
                request.getEmail(),
                request.getFailedAttempts(),
                REGISTRATION);
    }
    public static Otp requestToOtpForForgetPassword(OtpDataDTO request) {
        return new Otp(request.getOTP(),
                request.getEmail(),
                request.getFailedAttempts(),
                RESET_PASSWORD);
    }
    public static UserRegistrationRequest avroToRequest(User user, SetPasswordDTO request) {
        return UserRegistrationRequest.newBuilder()
                .setUserId(request.getUserId())
                .setName(user.getName().toString())
                .setEmail(user.getEmail().toString())
                .setContact(user.getContact().toString())
                .setDateOfBirth(user.getDateOfBirth().toString())
                .setPassword(request.getPassword())
                .build();
    }
    public static UserRegistrationResponse booleanToResponse(boolean success){
        return UserRegistrationResponse.newBuilder()
                .setSuccess(success)
                .build();
    }
    public static PasswordUpdateRequest dtoToRequest(ChangePasswordDTO changePasswordDTO){
        return PasswordUpdateRequest.newBuilder()
                .setEmail(changePasswordDTO.getEmail())
                .setPassword(changePasswordDTO.getPassword()).build();
    }
    public static UserCredentialRequest dtoToRequest(SetPasswordDTO dto){
        return UserCredentialRequest.newBuilder()
                .setUserId(dto.getUserId())
                .build();
    }

}
