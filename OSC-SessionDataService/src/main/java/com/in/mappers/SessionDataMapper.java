package com.in.mappers;

import com.in.dtos.*;
import com.in.entities.SessionEntity;
import com.in.proto.session.CreateSessionRequest;
import com.in.proto.session.CreateSessionResponse;
import com.in.proto.session.SessionDetailsResponse;
import com.in.proto.session.SessionLogoutRequest;
import com.in.proto.session.SessionValidationRequest;
import com.in.proto.session.SessionValidationResponse;

public class SessionDataMapper {
    /*public static SessionEntity dtoToEntity(CreateSessionDTO sessionData) {
        return new SessionEntity(sessionData.getUserId(), sessionData.getSessionId(), sessionData.getLoginDeviceType(), LocalDateTime.now(), null);
    }*/
    public static CreateSessionDTO requestToDto(CreateSessionRequest request) {
        return new CreateSessionDTO(request.getSessionId(),
                request.getUserId(),
                request.getLoginDeviceType());
    }
    public static CreateSessionResponse dtoToResponse(SessionDetailsDTO sessionDetailsDTO){
        return CreateSessionResponse.newBuilder().setSuccess(sessionDetailsDTO != null ).build();
    }

    public static LogoutRequestDTO requestToDTO(SessionValidationRequest request){
        return new LogoutRequestDTO(request.getUserId(),
                request.getSessionId());
    }


    public static SessionValidationResponse booleanToResponse(boolean isSessionActive){
        return SessionValidationResponse.newBuilder()
                .setSuccess(isSessionActive)
                .build();
    }
    public static SessionDataDTO entityToDto(SessionEntity sessionEntity){
        return new SessionDataDTO(sessionEntity.getUserId(), sessionEntity.getLoginDevice());
    }
    public static SessionDetailsResponse dtoToResponse(SessionDataDTO sessionData){
        return SessionDetailsResponse.newBuilder()
                .setUserId(sessionData.getUserId())
                .setLoginDeviceType(sessionData.getLoginDevice())
                .build();
    }
    public static LogoutRequestDTO requestToDTO(SessionLogoutRequest request){
        return new LogoutRequestDTO(request.getUserId(),
                request.getSessionId());
    }


}
