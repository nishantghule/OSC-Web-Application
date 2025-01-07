package com.in.grpcservices;

import com.in.dtos.SessionDataDTO;
import com.in.dtos.SessionDetailsDTO;
import com.in.mappers.SessionDataMapper;
import com.in.proto.session.CreateSessionRequest;
import com.in.proto.session.CreateSessionResponse;
import com.in.proto.session.SessionDetailsRequest;
import com.in.proto.session.SessionDetailsResponse;
import com.in.proto.session.SessionLogoutRequest;
import com.in.proto.session.SessionLogoutResponse;
import com.in.proto.session.SessionServiceGrpc;
import com.in.proto.session.SessionValidationRequest;
import com.in.proto.session.SessionValidationResponse;
import com.in.services.SessionDataService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@RequiredArgsConstructor
@GrpcService
public class SessionDataGrpcService extends SessionServiceGrpc.SessionServiceImplBase {
    private final Logger log = LogManager.getLogger(SessionDataGrpcService.class);
    private final SessionDataService sessionDataService;

    @Override
    public void createSession(CreateSessionRequest request, StreamObserver<CreateSessionResponse> responseObserver) {
        SessionDetailsDTO sessionDetailsDTO = sessionDataService.createSession(SessionDataMapper.requestToDto(request));
        log.info("session data stored into database {}", sessionDetailsDTO);
        responseObserver.onNext(SessionDataMapper.dtoToResponse(sessionDetailsDTO));
        responseObserver.onCompleted();
    }

    @Override
    public void validateSession(SessionValidationRequest request, StreamObserver<SessionValidationResponse> responseObserver) {
        boolean isSessionActive = sessionDataService.isSessionActive(SessionDataMapper.requestToDTO(request));
        log.info("is session active: {}", isSessionActive);
        responseObserver.onNext(SessionDataMapper.booleanToResponse(isSessionActive));
        responseObserver.onCompleted();
    }

    @Override
    public void getSessionDetails(SessionDetailsRequest request, StreamObserver<SessionDetailsResponse> responseObserver) {
        SessionDataDTO sessionData = sessionDataService.getSessionDetails(request.getSessionId());
        log.info("session data: {}", sessionData);
        responseObserver.onNext(SessionDataMapper.dtoToResponse(sessionData));
        responseObserver.onCompleted();
    }


    @Override
    public void isSessionLogout(SessionLogoutRequest request, StreamObserver<SessionLogoutResponse> responseObserver) {
        boolean isSessionLogout = sessionDataService.isSessionLogout(SessionDataMapper.requestToDTO(request));
        log.info("session is logged out: {}", isSessionLogout);
        responseObserver.onNext(SessionLogoutResponse.newBuilder().setSuccess(isSessionLogout).build());
        responseObserver.onCompleted();
    }
}
