/*
package com.in.controllers;

import com.in.dtos.ResponseCode;
import com.in.dtos.SetPasswordDTO;
import com.in.mapper.StatusCodeMapper;
import com.in.services.AddUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AddUserDetailsController {
    private final Logger log = LogManager.getLogger(AddUserDetailsController.class);
    private final AddUserDetailsService addUserDetailsService;

    @PostMapping("/addUserDetails")
    public ResponseEntity<ResponseCode> addUserDetails(@Valid @RequestBody SetPasswordDTO setPasswordDTO){
        log.info("Request received to add user details to database for userID: {} ", setPasswordDTO.getUserId());
        ResponseCode response = addUserDetailsService.addUserDetails(setPasswordDTO);
        return ResponseEntity.status(StatusCodeMapper.mapStatusCode(response.getCode())).body(response);
    }
}
*/
