package com.in.services.serviceimpls;

import com.in.dtos.LogoutRequestDTO;
import com.in.dtos.ResponseCode;
import com.in.kafka.avro.SessionKey;
import com.in.kafka.producer.SessionDataProducer;
import com.in.mapper.SessionDataMapper;
import com.in.mapper.StatusCodes;
import com.in.proto.session.SessionDetailsResponse;
import com.in.proto.session.SessionLogoutResponse;
import com.in.proto.session.SessionServiceGrpc;
import com.in.services.LogoutService;
import com.in.utility.SessionIdGenerator;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {
    private final Logger log = LogManager.getLogger(LogoutServiceImpl.class);
    private final SessionServiceGrpc.SessionServiceBlockingStub sessionDataStub;
    private final ReadOnlyKeyValueStore<SessionKey, String> sessionStore;
    private final SessionIdGenerator sessionIdGenerator;
    private final SessionDataProducer sessionDataProducer;
    @Override
    public ResponseCode logoutRequest(LogoutRequestDTO logoutRequestDTO) {
        try{
            SessionDetailsResponse sessionData = sessionDataStub.getSessionDetails(SessionDataMapper.dtoToRequest(logoutRequestDTO));
            log.info("sessionData received: {}",sessionData);
            if(sessionData == null){
               return new ResponseCode(StatusCodes.UNEXPECTED_ERROR);
            }
            SessionKey sessionKey = new SessionKey(sessionData.getUserId(), sessionData.getLoginDeviceType());
            log.info("sessionData: {}",sessionKey);
            String sessionId = sessionStore.get(sessionKey);
            log.info("sessionId received from ktable: {}",sessionId);
            if(!logoutRequestDTO.getSessionId().equals(sessionId) && !logoutRequestDTO.getUserId().equals(sessionData.getUserId())){
               return new ResponseCode(StatusCodes.UNEXPECTED_ERROR);
            }
            SessionLogoutResponse logoutResponse = sessionDataStub.isSessionLogout(SessionDataMapper.dtoToLogoutRequest(logoutRequestDTO));
            if(logoutResponse.getSuccess()){
                sessionDataProducer.removeDataFromTopic(sessionKey);
                return new ResponseCode(StatusCodes.LOGOUT_SUCCESS);
            }
        } catch (StatusRuntimeException e) {
            // Handling gRPC specific exceptions
            log.error("gRPC error while processing logout request for userId: {}", logoutRequestDTO.getUserId());
                return new ResponseCode(StatusCodes.UNEXPECTED_ERROR);
        } catch (Exception e) {
            // Handling other exceptions
            log.error("Unexpected error occurred while processing logout request for userId: {}", logoutRequestDTO.getUserId());
            return new ResponseCode(StatusCodes.UNEXPECTED_ERROR);
        }
        return new ResponseCode(StatusCodes.UNEXPECTED_ERROR);
    }
}
