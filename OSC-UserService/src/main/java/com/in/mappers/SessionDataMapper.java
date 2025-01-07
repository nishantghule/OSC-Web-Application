package com.in.mappers;

import com.in.dtos.LoginRequestDTO;
import com.in.dtos.LogoutRequestDTO;
import com.in.proto.user.UserCredentialRequest;
import com.in.kafka.avro.SessionKey;
import com.in.proto.session.CreateSessionRequest;
import com.in.proto.session.SessionDetailsRequest;
import com.in.proto.session.SessionLogoutRequest;
import com.in.proto.session.SessionValidationRequest;

public abstract class SessionDataMapper {
    public static UserCredentialRequest dtoToRequest(LoginRequestDTO loginRequestDTO){
        return UserCredentialRequest.newBuilder()
                .setUserId(loginRequestDTO.getUserId())
                .build();
    }
    public static SessionKey dtoToAvro(LoginRequestDTO loginRequestDTO){
        return SessionKey.newBuilder()
                .setUserId(loginRequestDTO.getUserId())
                .setLoginDeviceType(loginRequestDTO.getLoginDevice())
                .build();
    }
    public static CreateSessionRequest dtoToRequest(String sessionId, LoginRequestDTO loginRequestDTO){
        return CreateSessionRequest.newBuilder()
                .setSessionId(sessionId)
                .setUserId(loginRequestDTO.getUserId())
                .setLoginDeviceType(loginRequestDTO.getLoginDevice())
                .build();
    }
    public static SessionValidationRequest dtoToValidationRequest(LogoutRequestDTO request){
        return SessionValidationRequest.newBuilder()
                .setUserId(request.getUserId())
                .setSessionId(request.getSessionId())
                .build();
    }

    public static SessionDetailsRequest dtoToRequest(LogoutRequestDTO request){
        return SessionDetailsRequest.newBuilder()
                .setSessionId(request.getSessionId())
                .build();
    }
    public static SessionLogoutRequest dtoToLogoutRequest(LogoutRequestDTO request){
        return SessionLogoutRequest.newBuilder()
                .setUserId(request.getUserId())
                .setSessionId(request.getSessionId())
                .build();
    }

}
