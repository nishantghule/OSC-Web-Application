package com.in.services.serviceimpls;

import com.in.dtos.*;
import com.in.entities.SessionEntity;
import com.in.exceptions.ResourceNotFoundException;
import com.in.mappers.SessionDataMapper;
import com.in.repositories.SessionDataRepository;
import com.in.services.SessionDataService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service@RequiredArgsConstructor
public class SessionDataServiceImpl implements SessionDataService {
    private final SessionDataRepository sessionDataRepository;

    private final ModelMapper mapper;
    @Override
    public SessionDetailsDTO createSession(CreateSessionDTO createSessionDTO) {
        if(createSessionDTO == null){
            throw new ResourceNotFoundException("Request Body is empty");
        }
        //set login time on session creation
        SessionEntity sessionEntity = new SessionEntity(createSessionDTO.getSessionId(),
                createSessionDTO.getUserId(), createSessionDTO.getLoginDevice(),
                LocalDateTime.now(), null);
        // save session details to database
        SessionEntity savedSessionEntity = sessionDataRepository.save(sessionEntity);
        return mapper.map(savedSessionEntity, SessionDetailsDTO.class);
    }

    @Override
    public boolean isSessionLogout(LogoutRequestDTO logoutRequestDTO) {
        SessionEntity sessionEntity = sessionDataRepository.findById(logoutRequestDTO.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("session details not found"));
        if(logoutRequestDTO.getUserId().equals(sessionEntity.getUserId()) && logoutRequestDTO.getSessionId().equals(sessionEntity.getSessionId())){
            if(sessionEntity.getLoginTime() != null && sessionEntity.getLogoutTime() == null){
                sessionEntity.setLogoutTime(LocalDateTime.now());
                sessionDataRepository.save(sessionEntity);
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean isSessionActive(LogoutRequestDTO logoutRequestDTO) {
        SessionEntity sessionEntity = sessionDataRepository.findById(logoutRequestDTO.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("session details not found"));
        if (logoutRequestDTO.getUserId().equals(sessionEntity.getUserId()) && logoutRequestDTO.getSessionId().equals(sessionEntity.getSessionId())) {
            if(sessionEntity.getLoginTime() != null && sessionEntity.getLogoutTime() == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public SessionDataDTO getSessionDetails(String sessionId) {
        SessionEntity sessionEntity = sessionDataRepository.findById(sessionId).orElseThrow(()-> new ResourceNotFoundException("session not found"));
        return SessionDataMapper.entityToDto(sessionEntity);
    }
}
