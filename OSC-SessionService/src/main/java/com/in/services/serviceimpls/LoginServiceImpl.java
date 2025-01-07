package com.in.services.serviceimpls;

import com.in.dtos.LoginRequestDTO;
import com.in.dtos.Response;
import com.in.grpc.proto.user.UserCredentialResponse;
import com.in.grpc.proto.user.UserDataServiceGrpc;
import com.in.kafka.avro.SessionKey;
import com.in.kafka.producer.SessionDataProducer;
import com.in.mapper.SessionDataMapper;
import com.in.mapper.StatusCodes;
import com.in.proto.session.CreateSessionResponse;
import com.in.proto.session.SessionServiceGrpc;
import com.in.services.LoginService;
import com.in.utility.SessionIdGenerator;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final Logger log = LogManager.getLogger(LoginServiceImpl.class);
    private final UserDataServiceGrpc.UserDataServiceBlockingStub userDataStub;
    private final ReadOnlyKeyValueStore<SessionKey, String> sessionStore;
    private final SessionServiceGrpc.SessionServiceBlockingStub sessionDataStub;
    private final SessionIdGenerator sessionIdGenerator;
    private final SessionDataProducer sessionDataProducer;

    @Override
    public Response loginRequest(LoginRequestDTO loginRequestDTO) {
        try {
            // Fetch user credentials from user data service
            UserCredentialResponse userCredentials = userDataStub.verifyCredentials(SessionDataMapper.dtoToRequest(loginRequestDTO));

            // If user credentials are found
            if (userCredentials != null) {
                log.info("User credentials found for userId: {}", loginRequestDTO.getUserId());

                // password validation
                if (!userCredentials.getPassword().equals(loginRequestDTO.getPassword())) {
                    log.warn("Incorrect password for userId: {}", loginRequestDTO.getUserId());
                    return new Response(StatusCodes.PASSWORD_INCORRECT, null);
                }
                log.info("User credentials verified successfully for userId: {}", loginRequestDTO.getUserId());
                // checking for an active session for the user
                SessionKey sessionKey = SessionDataMapper.dtoToAvro(loginRequestDTO);
                String existingSessionId = sessionStore.get(sessionKey);

                // If there is an active session, returning an error
                if (existingSessionId != null) {
                    log.warn("Active session already exists for userId: {}", loginRequestDTO.getUserId());
                    return new Response(StatusCodes.ACTIVE_SESSION, null);
                }

                // Generate new session ID and create session
                String newSessionId = sessionIdGenerator.generateSessionId();
                log.info("Generated new session ID for userId {}: {}", loginRequestDTO.getUserId(), newSessionId);

                CreateSessionResponse sessionResponse = sessionDataStub.createSession(SessionDataMapper.dtoToRequest(newSessionId, loginRequestDTO));
                if (sessionResponse.getSuccess()) {
                    log.info("New session created successfully for userId: {}", loginRequestDTO.getUserId());

                    // Publish session data to Kafka
                    sessionDataProducer.publishDataToTopic(sessionKey, newSessionId);
                    log.info("Session data successfully published to Kafka for sessionId: {}", newSessionId);

                    // Return successful response with session ID and users name
                    return new Response(StatusCodes.LOGIN_SUCCESS, Map.of("sessionId", newSessionId, "name", userCredentials.getName()));
                } else {
                    log.error("Failed to create session for userId: {}", loginRequestDTO.getUserId());
                    return new Response(StatusCodes.UNEXPECTED_ERROR, null);
                }
            } else {
                // User not found in the user data service
                log.error("User not found for userId: {}", loginRequestDTO.getUserId());
                return new Response(StatusCodes.USERID_INCORRECT, null);
            }
        } catch (StatusRuntimeException e) {
            // Handling gRPC specific exceptions
            log.error("gRPC error while processing login request for userId: {}", loginRequestDTO.getUserId());
            if (e.getStatus().getCode() == io.grpc.Status.Code.NOT_FOUND) {
                // Handling user not found error
                return new Response(StatusCodes.USERID_INCORRECT, null);
            } else {
                // Handling other gRPC errors
                return new Response(StatusCodes.UNEXPECTED_ERROR, null);
            }
        } catch (Exception e) {
            // Handling other exceptions
            log.error("Unexpected error occurred while processing login request for userId: {}", loginRequestDTO.getUserId());
            return new Response(StatusCodes.UNEXPECTED_ERROR, null);
        }
    }
}

