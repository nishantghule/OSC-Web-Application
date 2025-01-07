/*
package com.in.controllers;

import com.in.dtos.ChangePasswordDTO;
import com.in.dtos.ResponseCode;
import com.in.mapper.StatusCodeMapper;
import com.in.services.ChangePasswordService;
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
public class ChangePasswordController {
    private final Logger log = LogManager.getLogger(ChangePasswordController.class);
    private final ChangePasswordService changePasswordService;

    @PostMapping("/changePassword")
    public ResponseEntity<ResponseCode> addUserDetails(@Valid @RequestBody ChangePasswordDTO changePasswordDTO){
        log.info("Request received to change password for user with email: {} ", changePasswordDTO.getEmail());
        ResponseCode response = changePasswordService.changePassword(changePasswordDTO);
        return ResponseEntity.status(StatusCodeMapper.mapStatusCode(response.getCode())).body(response);
    }
}
*/
