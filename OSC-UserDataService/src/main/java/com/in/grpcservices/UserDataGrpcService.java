package com.in.grpcservices;

import com.in.dtos.LoginDTO;
import com.in.dtos.UserDTO;
import com.in.mappers.UserDataMapper;

import com.in.proto.user.PasswordUpdateRequest;
import com.in.proto.user.PasswordUpdateResponse;
import com.in.proto.user.UserCredentialResponse;
import com.in.proto.user.UserDataServiceGrpc;
import com.in.proto.user.UserEmailRequest;
import com.in.proto.user.UserEmailResponse;
import com.in.proto.user.UserRegistrationResponse;
import com.in.services.UserDataService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RequiredArgsConstructor
@GrpcService
public class UserDataGrpcService extends UserDataServiceGrpc.UserDataServiceImplBase {

    private final UserDataService userDataService;
    private final Logger log = LogManager.getLogger(UserDataGrpcService.class);

    @Override
    public void saveUserDetails(com.in.proto.user.UserRegistrationRequest request, StreamObserver<com.in.proto.user.UserRegistrationResponse> responseObserver) {
        UserDTO userDTO = UserDataMapper.requestToDTO(request);
        UserDTO savedUser = userDataService.saveUser(userDTO);

        UserRegistrationResponse response = UserDataMapper.dtoToResponse(savedUser);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void checkEmailExists(UserEmailRequest request, StreamObserver<UserEmailResponse> responseObserver) {
        boolean exists = userDataService.checkEmailExists(request.getEmail());
        UserEmailResponse response = UserDataMapper.dtoToResponse(exists);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void verifyCredentials(com.in.proto.user.UserCredentialRequest request, StreamObserver<UserCredentialResponse> responseObserver) {
        log.info("Verifying login credentials for userId: {}", request.getUserId());
        LoginDTO loginDetails = userDataService.getUserLoginDetails(request.getUserId());

        responseObserver.onNext(UserDataMapper.dtoToResponse(loginDetails));
        responseObserver.onCompleted();
    }


    @Override
    public void updatePassword(PasswordUpdateRequest request, StreamObserver<PasswordUpdateResponse> responseObserver) {
        boolean success = userDataService.updatePassword(request.getEmail(), request.getPassword());
        PasswordUpdateResponse response = PasswordUpdateResponse.newBuilder().setSuccess(success).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
