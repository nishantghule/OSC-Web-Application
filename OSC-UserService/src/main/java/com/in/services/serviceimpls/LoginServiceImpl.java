package com.in.services.serviceimpls;

import com.in.dtos.LoginRequestDTO;
import com.in.dtos.Response;
import com.in.proto.user.UserCredentialResponse;
import com.in.proto.user.UserDataServiceGrpc;
import com.in.kafka.avro.SessionKey;
import com.in.kafka.producers.SessionDataProducer;
import com.in.mappers.SessionDataMapper;
import com.in.mappers.StatusCodes;
import com.in.proto.session.CreateSessionResponse;
import com.in.proto.session.SessionServiceGrpc;
import com.in.services.LoginService;
import com.in.utility.SessionIdGenerator;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    private final Logger log = LogManager.getLogger(LoginServiceImpl.class);
    private final UserDataServiceGrpc.UserDataServiceBlockingStub userDataStub;
    @Qualifier("sessionStore")
    private final ReadOnlyKeyValueStore<SessionKey, String> sessionStore;
    private final SessionServiceGrpc.SessionServiceBlockingStub sessionDataStub;
    private final SessionIdGenerator sessionIdGenerator;
    private final SessionDataProducer sessionDataProducer;

    public LoginServiceImpl(UserDataServiceGrpc.UserDataServiceBlockingStub userDataStub, @Qualifier("sessionStore") ReadOnlyKeyValueStore<SessionKey, String> sessionStore, SessionServiceGrpc.SessionServiceBlockingStub sessionDataStub, SessionIdGenerator sessionIdGenerator, SessionDataProducer sessionDataProducer) {
        this.userDataStub = userDataStub;
        this.sessionStore = sessionStore;
        this.sessionDataStub = sessionDataStub;
        this.sessionIdGenerator = sessionIdGenerator;
        this.sessionDataProducer = sessionDataProducer;
    }


    @Override
    public Response loginRequest(LoginRequestDTO loginRequestDTO) {
        log.info("Login attempt for userId: {}", loginRequestDTO.getUserId());

        // Fetch user credentials from user data service
        UserCredentialResponse userCredentials = userDataStub.verifyCredentials(SessionDataMapper.dtoToRequest(loginRequestDTO));

        if (userCredentials != null) {
            log.info("User credentials found for userId: {}", loginRequestDTO.getUserId());

            // Password validation
            if (!userCredentials.getPassword().equals(loginRequestDTO.getPassword())) {
                log.warn("Incorrect password for userId: {}", loginRequestDTO.getUserId());
                return new Response(StatusCodes.PASSWORD_INCORRECT, null);
            }

            log.info("User credentials verified successfully for userId: {}", loginRequestDTO.getUserId());

            // Check for active session
            SessionKey sessionKey = SessionDataMapper.dtoToAvro(loginRequestDTO);
            String existingSessionId = sessionStore.get(sessionKey);

            if (existingSessionId != null) {
                log.warn("Active session already exists for userId: {}", loginRequestDTO.getUserId());
                return new Response(StatusCodes.ACTIVE_SESSION, null);
            }

            // Generate new session ID and create session
            String newSessionId = sessionIdGenerator.generateSessionId();
            log.info("Generated new session ID for userId {}: {}", loginRequestDTO.getUserId(), newSessionId);

            // Create session via gRPC call
            CreateSessionResponse sessionResponse = sessionDataStub.createSession(SessionDataMapper.dtoToRequest(newSessionId, loginRequestDTO));

            if (sessionResponse.getSuccess()) {
                log.info("New session created successfully for userId: {}", loginRequestDTO.getUserId());

                // Publish session data to Kafka
                sessionDataProducer.publishDataToTopic(sessionKey, newSessionId);
                log.info("Session data successfully published to Kafka for sessionId: {}", newSessionId);

                // Return successful response with session ID and user's name
                return new Response(StatusCodes.LOGIN_SUCCESS, Map.of("sessionId", newSessionId, "name", userCredentials.getName()));
            } else {
                log.error("Failed to create session for userId: {}", loginRequestDTO.getUserId());
                return new Response(StatusCodes.UNEXPECTED_ERROR, null);
            }
        } else {
            log.error("User not found for userId: {}", loginRequestDTO.getUserId());
            return new Response(StatusCodes.USERID_INCORRECT, null);
        }
    }
}

