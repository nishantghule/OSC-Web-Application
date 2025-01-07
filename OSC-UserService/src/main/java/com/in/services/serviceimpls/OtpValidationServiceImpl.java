package com.in.services.serviceimpls;

import com.in.dtos.ForgetPasswordRequestDTO;
import com.in.dtos.OtpValidationDTO;
import com.in.dtos.ResponseCode;
import com.in.exceptions.UserNotFoundException;
import com.in.kafka.avro.Otp;
import com.in.kafka.producers.OtpProducer;
import com.in.kafka.producers.UserDataProducer;
import com.in.mappers.StatusCodes;
import com.in.services.OtpValidationService;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class OtpValidationServiceImpl implements OtpValidationService {
    private final Logger log = LogManager.getLogger(OtpValidationServiceImpl.class);
    @Qualifier("userOtpStore")
    private final ReadOnlyKeyValueStore<String, Otp> otpStore;
    private final UserDataProducer userDataProducer;
    private final OtpProducer otpProducer;

    public OtpValidationServiceImpl(@Qualifier("userOtpStore") ReadOnlyKeyValueStore<String, Otp> otpStore, UserDataProducer userDataProducer, OtpProducer otpProducer) {
        this.otpStore = otpStore;
        this.userDataProducer = userDataProducer;
        this.otpProducer = otpProducer;
    }

    @Override
    public ResponseCode validateOtpOnRegistration(OtpValidationDTO request) {
        log.info("Starting OTP validation on REGISTRATION for userId: {}", request.getUserId());

        Otp otpData = otpStore.get(request.getUserId());
        if (otpData == null) {
            log.warn("No OTP record found for userId: {}", request.getUserId());
            throw new UserNotFoundException(StatusCodes.USERID_INCORRECT);
        }
        if (!otpData.getOtp().toString().equals(request.getOTP())) {
            //increment failedAttempts by 1 on wrong otp
            int failedAttempts = otpData.getFailedAttempts() + 1;
            log.warn("Invalid OTP attempts for userId: {}, failedAttempts: {}", request.getUserId(), failedAttempts);

            //update the otp failedAttempts on each failure
            otpData.setFailedAttempts(failedAttempts);
            updateFailedOtpAttempts(request.getUserId(), otpData);

            if (failedAttempts >= 3) {
                log.error("Maximum failed OTP attempts reached for userId: {}. Deleting user and OTP data.", request.getUserId());
                userDataProducer.removeUserDataFromTopic(request.getUserId());
                otpProducer.removeOtpDataFromTopic(request.getUserId());
                return new ResponseCode(StatusCodes.MAX_FAILED_ATTEMPTS);
            }
            //if failedAttempts < 3 but otp is invalid
            return new ResponseCode(StatusCodes.INVALID_OTP);
        }
        log.info("OTP validated successfully on REGISTRATION for userId: {}", request.getUserId());
        return new ResponseCode(StatusCodes.VALID_OTP);
    }

    @Override
    public ResponseCode validateOtpOnForgetPassword(ForgetPasswordRequestDTO forgetPasswordRequestDTO) {
        log.info("Starting OTP validation on forget password for email: {}", forgetPasswordRequestDTO.getEmail());

        Otp otpData = otpStore.get(forgetPasswordRequestDTO.getEmail());
        if (otpData == null) {
            log.warn("No OTP record found for email: {}", forgetPasswordRequestDTO.getEmail());
            return new ResponseCode(StatusCodes.INVALID_EMAIL);
        }
        if (!otpData.getOtp().toString().equals(forgetPasswordRequestDTO.getOTP())) {
            log.info("otp not matched:{}", otpData);
            return new ResponseCode(StatusCodes.OTP_INVALID);
        }
        log.info("OTP validated successfully on FORGET-PASSWORD for email: {}", otpData );
        return new ResponseCode(StatusCodes.CORRECT_OTP);
    }

    private void updateFailedOtpAttempts(String userId, Otp otpData) {
        if (otpData != null) {
            Otp updatedOtpMessage = Otp.newBuilder(otpData)
                    .setFailedAttempts(otpData.getFailedAttempts())
                    .build();

            log.info("Updating failed attempts for userId: {}, failedAttempts: {}", userId, otpData.getFailedAttempts());
            otpProducer.publishToTopic(userId, updatedOtpMessage);
        } else {
            log.warn("No OTP record to update for userId: {}", userId);
        }
    }

}
