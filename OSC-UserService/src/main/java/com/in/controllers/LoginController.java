/*
package com.in.controllers;

import com.in.dtos.LoginRequestDTO;
import com.in.dtos.Response;
import com.in.mapper.StatusCodeMapper;
import com.in.services.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class LoginController {
    private final LoginService loginService;
    @PostMapping("/login")
    public ResponseEntity<Response> loginRequest(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        Response response = loginService.loginRequest(loginRequestDTO);
        return ResponseEntity.status(StatusCodeMapper.mapStatusCode(response.getCode()))
                .body(response);
    }
}
*/
