package com.in.services.serviceimpls;

import com.in.dtos.ResponseCode;
import com.in.dtos.SetPasswordDTO;
import com.in.exceptions.UserNotFoundException;
import com.in.proto.user.UserDataServiceGrpc;
import com.in.proto.user.UserRegistrationResponse;
import com.in.kafka.avro.User;
import com.in.kafka.producers.OtpProducer;
import com.in.kafka.producers.UserDataProducer;
import com.in.mappers.StatusCodes;
import com.in.mappers.UserMapper;
import com.in.services.AddUserDetailsService;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AddUserDetailsServiceImpl implements AddUserDetailsService {
    private final Logger log = LogManager.getLogger(AddUserDetailsServiceImpl.class);

    @Qualifier("userDetailsStore")
    private final ReadOnlyKeyValueStore<String, User> userStore;

    private final UserDataServiceGrpc.UserDataServiceBlockingStub userDataStub;
    private final UserDataProducer userDataProducer;
    private final OtpProducer otpProducer;

    public AddUserDetailsServiceImpl(@Qualifier("userDetailsStore") ReadOnlyKeyValueStore<String, User> userStore,
                                     UserDataServiceGrpc.UserDataServiceBlockingStub userDataStub,
                                     UserDataProducer userDataProducer,
                                     OtpProducer otpProducer) {
        this.userStore = userStore;
        this.userDataStub = userDataStub;
        this.userDataProducer = userDataProducer;
        this.otpProducer = otpProducer;
    }

    @Override
    public ResponseCode addUserDetails(SetPasswordDTO setPasswordDTO) {
        String userId = setPasswordDTO.getUserId();
        // Fetch user from the store
        User user = userStore.get(setPasswordDTO.getUserId());
        if (user == null) {
            log.error("User not found for userId: {}. Unable to proceed with setting password.", userId);
            throw new UserNotFoundException(StatusCodes.USER_ID_NOT_FOUND);
        }
        log.info("User found for userId: {}. Proceeding with user details update.", userId);
        // save user details to database
        UserRegistrationResponse userRegistrationResponse = userDataStub.saveUserDetails(UserMapper.avroToRequest(user, setPasswordDTO));

        boolean success = userRegistrationResponse.getSuccess();
        if (!success) {
            log.error("Failed to save user details for userId: {}. User creation failed.", userId);
            return new ResponseCode(StatusCodes.USER_CREATION_FAILED);
        }

        log.info("User registration successful for UserId: {}", userId);
        // If user saved successfully, remove user data from Kafka topics
        log.info("User details successfully saved for userId: {}. Removing user data from Kafka topics.", userId);
        userDataProducer.removeUserDataFromTopic(userId);
        otpProducer.removeOtpDataFromTopic(userId);

        log.info("User details for userId: {} processed successfully.", userId);
        return new ResponseCode(StatusCodes.USER_CREATION_SUCCESS);
    }
}
