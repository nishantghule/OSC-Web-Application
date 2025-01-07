package com.in.services.serviceimpls;

import com.in.dtos.ChangePasswordDTO;
import com.in.dtos.ResponseCode;
import com.in.proto.user.PasswordUpdateResponse;
import com.in.proto.user.UserDataServiceGrpc;
import com.in.proto.user.UserEmailRequest;
import com.in.proto.user.UserEmailResponse;
import com.in.mappers.StatusCodes;
import com.in.mappers.UserMapper;
import com.in.services.ChangePasswordService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangePasswordServiceImpl implements ChangePasswordService {
    private final Logger log = LogManager.getLogger(ChangePasswordServiceImpl.class);
    private final UserDataServiceGrpc.UserDataServiceBlockingStub userDataStub;

    @Override
    public ResponseCode changePassword(ChangePasswordDTO changePasswordDTO) {
        log.info("Received change password request for email: {}", changePasswordDTO.getEmail());

        log.info("Checking if email exists: {}", changePasswordDTO.getEmail());
        UserEmailResponse emailResponse = userDataStub.checkEmailExists(UserEmailRequest.newBuilder().setEmail(changePasswordDTO.getEmail()).build());

        if (!emailResponse.getExists()) {
            log.warn("Email not found in the system: {}", changePasswordDTO.getEmail());
            return new ResponseCode(StatusCodes.INVALID_EMAIL);
        }

        log.info("Email exists for: {}", changePasswordDTO.getEmail());

        log.info("Attempting to update password for email: {}", changePasswordDTO.getEmail());
        PasswordUpdateResponse passwordUpdateResponse = userDataStub.updatePassword(UserMapper.dtoToRequest(changePasswordDTO));

        if (!passwordUpdateResponse.getSuccess()) {
            log.error("Password update failed for email: {}", changePasswordDTO.getEmail());
            return new ResponseCode(StatusCodes.PASSWORD_NOT_SAVED);
        }

        log.info("Password successfully updated for email: {}", changePasswordDTO.getEmail());
        return new ResponseCode(StatusCodes.PASSWORD_SAVED);
    }
}
