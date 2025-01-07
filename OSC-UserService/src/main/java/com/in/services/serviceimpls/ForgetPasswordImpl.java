package com.in.services.serviceimpls;

import com.in.dtos.EmailDTO;
import com.in.dtos.OtpDataDTO;
import com.in.dtos.ResponseCode;
import com.in.proto.user.UserDataServiceGrpc;
import com.in.proto.user.UserEmailRequest;
import com.in.proto.user.UserEmailResponse;
import com.in.kafka.producers.OtpProducer;
import com.in.mappers.StatusCodes;
import com.in.services.ForgetPasswordService;
import com.in.utility.OtpGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForgetPasswordImpl implements ForgetPasswordService {
    private final Logger log = LogManager.getLogger(ForgetPasswordImpl.class);
    private final UserDataServiceGrpc.UserDataServiceBlockingStub userDataStub;
    private final OtpGenerator otpGenerator;
    private final OtpProducer otpProducer;

    @Override
    public ResponseCode forgetPassword(EmailDTO email) {
        log.info("Processing forget password request for email: {}", email.getEmail());

        // Check if the email exists in the system
        UserEmailResponse response = userDataStub.checkEmailExists(UserEmailRequest.newBuilder().setEmail(email.getEmail()).build());

        if (!response.getExists()) {
            log.warn("Invalid email entered: {}", email.getEmail());
            return new ResponseCode(StatusCodes.INVALID_EMAIL);
        }

        // Generate OTP for the valid email
        String otp = otpGenerator.generateOtp();
        log.info("Generated OTP for email {}: {}", email.getEmail(), otp);

        // Prepare OTP data to send to Kafka
        OtpDataDTO otpDataToSend = new OtpDataDTO("", otp, email.getEmail(), 0);

        // Publish OTP to Kafka for sending email
        boolean emailSent = otpProducer.publishToOtpTopicForForgetPassword(otpDataToSend);
        if (emailSent) {
            log.info("OTP sent successfully for email: {}", email.getEmail());
            return new ResponseCode(StatusCodes.VALID_EMAIL);
        }
        log.error("Failed to send OTP to Kafka for email: {}", email.getEmail());
        return new ResponseCode(StatusCodes.UNEXPECTED_ERROR);
    }
}
