package com.in.services.serviceimpls;

import com.in.dtos.OtpDataDTO;
import com.in.dtos.RegistrationDTO;
import com.in.dtos.Response;
import com.in.kafka.producers.OtpProducer;
import com.in.kafka.producers.UserDataProducer;
import com.in.mappers.StatusCodes;
import com.in.mappers.UserMapper;
import com.in.proto.user.UserDataServiceGrpc;
import com.in.proto.user.UserEmailRequest;
import com.in.proto.user.UserEmailResponse;
import com.in.services.UserRegistrationService;
import com.in.utility.OtpGenerator;
import com.in.utility.UserIdGenerator;
import com.in.exceptions.EmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {
    private final UserDataServiceGrpc.UserDataServiceBlockingStub userDataStub;
    private final Logger log = LogManager.getLogger(UserRegistrationServiceImpl.class);
    private final UserIdGenerator userIdGenerator;
    private final OtpGenerator otpGenerator;
    private final UserDataProducer userDataProducer;
    private final OtpProducer otpProducer;

    @Override
    public Response registerUser(RegistrationDTO registrationDTO) {
        log.info("Starting user registration for email: {}", registrationDTO.getEmail());

        // Check if email is unique, throw custom exception if not
        if (!checkEmailIsUnique(registrationDTO.getEmail())) {
            log.warn("Email already exists: {}", registrationDTO.getEmail());
            throw new EmailAlreadyExistsException(registrationDTO.getEmail());
        }

        // Generate userId and OTP if the email is unique
        String userId = userIdGenerator.generateUserId();
        String otp = otpGenerator.generateOtp();
        log.info("Generated UserId: {}, OTP: {}", userId, otp);

        // Publish user details to Kafka topic
        log.info("Publishing user details to Kafka for UserId: {}", userId);
        userDataProducer.publishToTopic(userId, UserMapper.dtoToAvro(registrationDTO));

        // Publish OTP to Kafka topic
        log.info("Publishing OTP to Kafka for UserId: {}", userId);
        boolean emailSent = otpProducer.publishToOtpTopicForRegistration(new OtpDataDTO(userId, otp, registrationDTO.getEmail(), 0));

        if (!emailSent) {
            log.error("Failed to send OTP email for UserId: {}", userId);
            return new Response(StatusCodes.USER_CREATION_FAILED, null);
        }

        log.info("User registration successful for UserId: {}", userId);
        return new Response(StatusCodes.USER_CREATION_SUCCESS, Map.of("userId", userId));
    }

    // Method to check if the email is unique using the gRPC service
    private boolean checkEmailIsUnique(String email) {
            log.info("Checking if email exists in the system: {}", email);
            UserEmailRequest request = UserMapper.emailToRequest(email);
            UserEmailResponse response = userDataStub.checkEmailExists(request);
            boolean isUnique = !response.getExists();  // If exists is true, email is not unique
            log.info("Email check result for {}: {}", email, isUnique ? "Unique" : "Exists");
            return isUnique;
    }
}
