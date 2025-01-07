/*
package com.in.controllers;

import com.in.dtos.LogoutRequestDTO;
import com.in.dtos.ResponseCode;
import com.in.mapper.StatusCodeMapper;
import com.in.services.LogoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class LogoutController {
    private final LogoutService logoutService;

    @PostMapping("/logout")
    public ResponseEntity<ResponseCode> logoutRequest(@Valid @RequestBody LogoutRequestDTO logoutRequestDTO){
        ResponseCode response = logoutService.logoutRequest(logoutRequestDTO);
        return ResponseEntity.status(StatusCodeMapper.mapStatusCode(response.getCode())).body(response);
    }
}
*/
