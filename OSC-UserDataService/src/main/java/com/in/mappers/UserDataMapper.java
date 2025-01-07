package com.in.mappers;

import com.in.dtos.LoginDTO;
import com.in.dtos.UserDTO;
import com.in.proto.user.PasswordUpdateResponse;
import com.in.proto.user.UserCredentialResponse;
import com.in.proto.user.UserEmailRequest;
import com.in.proto.user.UserEmailResponse;
import com.in.proto.user.UserRegistrationRequest;
import com.in.proto.user.UserRegistrationResponse;

import java.time.LocalDate;

public abstract class UserDataMapper {

    // Map UserRegistrationRequest (gRPC) to UserDTO
    public static UserDTO requestToDTO(UserRegistrationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        UserDTO dto = new UserDTO();
        dto.setUserId(request.getUserId());
        dto.setName(request.getName());
        dto.setEmail(request.getEmail());
        dto.setContact(request.getContact());
        dto.setDOB(LocalDate.parse(request.getDateOfBirth()));
        dto.setPassword(request.getPassword());
        return dto;
    }

    // Map UserDTO to UserRegistrationResponse (gRPC)
    public static UserRegistrationResponse dtoToResponse(UserDTO userDTO) {
        return UserRegistrationResponse.newBuilder()
                .setSuccess(userDTO != null)
                .build();
    }


    // Map UserEmailRequest (gRPC) to UserDTO
    public static String requestToDTO(UserEmailRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        return request.getEmail();
    }


    // Map boolean to UserEmailResponse (gRPC)
    public static UserEmailResponse dtoToResponse(boolean exists) {
        return UserEmailResponse.newBuilder()
                .setExists(exists)
                .build();
    }

    // Map loginDTO to UserCredentialResponse (gRPC)
    public static com.in.proto.user.UserCredentialResponse dtoToResponse(LoginDTO loginDTO) {
        return UserCredentialResponse.newBuilder()
                .setUserId(loginDTO.getUserId())
                .setName(loginDTO.getName())
                .setPassword(loginDTO.getPassword())
                .build();
    }


    // Map boolean to PasswordUpdateResponse (gRPC)
    public static PasswordUpdateResponse mapToPasswordUpdateResponse(boolean success) {
        return PasswordUpdateResponse.newBuilder()
                .setSuccess(success)
                .build();
    }
}
