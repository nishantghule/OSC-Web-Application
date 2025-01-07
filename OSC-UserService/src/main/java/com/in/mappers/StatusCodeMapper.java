package com.in.mappers;

import org.springframework.http.HttpStatus;

public class StatusCodeMapper {
    public static HttpStatus mapStatusCode(int code) {
        switch (code) {
            case 30:      // Email already registered
                return HttpStatus.OK;
            case 200:     // Success (OTP correct and user creation success)
                return HttpStatus.OK;
            case 220:     // User creation failed (bad request)
                return HttpStatus.BAD_REQUEST;
            case 301:     // Maximum OTP attempts reached
                return HttpStatus.OK;
            case 502:     // Invalid OTP
                return HttpStatus.OK;
            case 1999:    // UserId not found
                return HttpStatus.NOT_FOUND;
            case 199:     // Wrong Email or OTP invalid
                return HttpStatus.OK;
            case 500:     // OTP validated successfully
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
