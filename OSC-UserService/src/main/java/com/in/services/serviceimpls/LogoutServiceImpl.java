package com.in.services.serviceimpls;

import com.in.dtos.LogoutRequestDTO;
import com.in.dtos.ResponseCode;
import com.in.kafka.avro.SessionKey;
import com.in.kafka.producers.SessionDataProducer;
import com.in.mappers.SessionDataMapper;
import com.in.mappers.StatusCodes;
import com.in.proto.cart.CartDataServiceGrpc;
import com.in.proto.cart.ViewCartProductRequest;
import com.in.proto.session.SessionDetailsResponse;
import com.in.proto.session.SessionLogoutResponse;
import com.in.proto.session.SessionServiceGrpc;
import com.in.proto.session.SessionValidationResponse;
import com.in.services.LogoutService;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class LogoutServiceImpl implements LogoutService {
    private final Logger log = LogManager.getLogger(LogoutServiceImpl.class);
    private final SessionServiceGrpc.SessionServiceBlockingStub sessionDataStub;
    @Qualifier("sessionStore")
    private final ReadOnlyKeyValueStore<SessionKey, String> sessionStore;
    private final SessionDataProducer sessionDataProducer;
    private final CartDataServiceGrpc.CartDataServiceBlockingStub cartDataServiceStub;

    public LogoutServiceImpl(SessionServiceGrpc.SessionServiceBlockingStub sessionDataStub, @Qualifier("sessionStore") ReadOnlyKeyValueStore<SessionKey, String> sessionStore, SessionDataProducer sessionDataProducer, CartDataServiceGrpc.CartDataServiceBlockingStub cartDataServiceStub) {
        this.sessionDataStub = sessionDataStub;
        this.sessionStore = sessionStore;
        this.sessionDataProducer = sessionDataProducer;
        this.cartDataServiceStub = cartDataServiceStub;
    }

    @Override
    public ResponseCode logoutRequest(LogoutRequestDTO logoutRequestDTO) {
        log.info("Processing logout request for userId: {}", logoutRequestDTO.getUserId());

        // Fetch session data from session service
        SessionDetailsResponse sessionData = sessionDataStub.getSessionDetails(SessionDataMapper.dtoToRequest(logoutRequestDTO));

        // Check if session data is found
        if (sessionData == null) {
            log.error("Session data not found for userId: {}", logoutRequestDTO.getUserId());
            return new ResponseCode(StatusCodes.UNEXPECTED_ERROR);  // Consistent error response
        }

        SessionKey sessionKey = new SessionKey(sessionData.getUserId(), sessionData.getLoginDeviceType());
        log.info("Session data: {}", sessionKey);

        // Fetch session ID from the session store
        String sessionId = sessionStore.get(sessionKey);
        log.info("Session ID received from session store: {}", sessionId);
        if(sessionId == null){
            //if sessionId is null either kafka is down or topic deleted then check into db
            SessionValidationResponse response = sessionDataStub.validateSession(SessionDataMapper.dtoToValidationRequest(logoutRequestDTO));
            boolean isActive = response.getSuccess();
            log.info("Validating user is active from database isActive: {}", isActive);
            if(!isActive){
                return new ResponseCode(StatusCodes.UNEXPECTED_ERROR);//if user is logged out already checked from db
            }
        }
        // Validate if the session ID in the request matches the stored session ID
        if (!logoutRequestDTO.getSessionId().equals(sessionId) && !logoutRequestDTO.getUserId().equals(sessionData.getUserId())) {
            log.warn("Session ID mismatch for userId: {}", logoutRequestDTO.getUserId());
            return new ResponseCode(StatusCodes.UNEXPECTED_ERROR);
        }

        // Proceed to logout session request
        SessionLogoutResponse logoutResponse = sessionDataStub.isSessionLogout(SessionDataMapper.dtoToLogoutRequest(logoutRequestDTO));

        // Check if session logout was successful
        if (logoutResponse.getSuccess()) {
            log.info("Logout successful for userId: {}", logoutRequestDTO.getUserId());

            // Remove session data from Kafka topic
            sessionDataProducer.removeDataFromTopic(sessionKey);
            log.info("Session data removed from Kafka topic for sessionId: {}", sessionId);
            //ViewCartProductResponse cartDataResponse = cartDataServiceStub.fetchProductsOfCart(ViewCartProductRequest.newBuilder().setUserId(logoutRequestDTO.getUserId()).build());
           // if(!cartDataResponse.getCartProductsList().isEmpty()){
                //add cart data for logout user to database
                cartDataServiceStub.saveCartProductsToDB(ViewCartProductRequest.newBuilder().setUserId(logoutRequestDTO.getUserId()).build());
          //  }
            log.info("Cart data saved from Kafka topic to database for userId: {}", logoutRequestDTO.getUserId());
            return new ResponseCode(StatusCodes.LOGOUT_SUCCESS);
        }
        log.error("Logout failed for userId: {}", logoutRequestDTO.getUserId());
        return new ResponseCode(StatusCodes.UNEXPECTED_ERROR);
    }

}
