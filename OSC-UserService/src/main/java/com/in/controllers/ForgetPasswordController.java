/*
package com.in.controllers;

import com.in.dtos.EmailDTO;
import com.in.dtos.ResponseCode;
import com.in.mapper.StatusCodeMapper;
import com.in.services.ForgetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class ForgetPasswordController {
    private final ForgetPasswordService forgetPasswordService;
    @PostMapping("/forgotPassword")
    public ResponseEntity<ResponseCode> forgetPassword(@RequestBody EmailDTO email){
        ResponseCode response = forgetPasswordService.forgetPassword(email);
        return ResponseEntity.status(StatusCodeMapper.mapStatusCode(response.getCode())).body(response);
    }
}
*/
