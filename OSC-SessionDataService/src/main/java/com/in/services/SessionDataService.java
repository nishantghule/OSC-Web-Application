package com.in.services;

import com.in.dtos.*;

public interface SessionDataService {
    SessionDetailsDTO createSession(CreateSessionDTO createSessionDTO);
    // isSessionLogout(LogoutRequestDTO logoutRequestDTO);
    boolean isSessionActive(LogoutRequestDTO logoutRequestDTO);
    SessionDataDTO getSessionDetails(String sessionId);
    boolean isSessionLogout(LogoutRequestDTO logoutRequestDTO);
}
