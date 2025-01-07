package com.in.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class StatusCodeMapper {
    public static HttpStatus mapStatusCode(int code) {
        switch (code) {
            case 200:     // Success (both OTP user Login and Logout)
                return HttpStatus.OK;
            case 201:     // Wrong UserId (bad request)
                return HttpStatus.OK;
            case 202:     // Wrong Password (bad request)
                return HttpStatus.OK;
            case 204:     // User creation failed (bad request)
                return HttpStatus.OK;
            case 0:       // Internal Server Error
                return HttpStatus.INTERNAL_SERVER_ERROR;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
