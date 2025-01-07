/*
package com.in.controllers;

import com.in.dtos.RegistrationDTO;
import com.in.dtos.Response;
import com.in.mapper.StatusCodeMapper;
import com.in.services.UserRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserRegistrationController {
    private final Logger log = LogManager.getLogger(UserRegistrationController.class);
    private final UserRegistrationService userRegistrationService;

    @PostMapping("/signup")
    public ResponseEntity<?> userRegistration(@Valid @RequestBody RegistrationDTO registrationDTO){
        log.info("Received request to create user: {}", registrationDTO);

        Response response = userRegistrationService.registerUser(registrationDTO);

        // Return the appropriate response based on the service result
        return ResponseEntity.status(StatusCodeMapper.mapStatusCode(response.getCode()))
                .body(response);
    }
}
*/
