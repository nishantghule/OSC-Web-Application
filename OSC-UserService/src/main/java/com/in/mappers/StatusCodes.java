package com.in.mappers;

public interface StatusCodes {
    int EMAIL_ALREADY_EXISTS = 30;
    int UNEXPECTED_ERROR = 0;
    int VALID_EMAIL = 200;
    int INVALID_EMAIL = 199;
    int USER_CREATION_FAILED = 220;
    int USER_ID_NOT_FOUND = 1999;
    int INVALID_OTP = 502;
    int MAX_FAILED_ATTEMPTS = 301;
    int VALID_OTP = 500;
    int USER_CREATION_SUCCESS = 200;
    int CORRECT_OTP = 200;
    int OTP_INVALID = 199;
    int PASSWORD_SAVED = 200;
    int PASSWORD_NOT_SAVED = 199;
    int LOGIN_SUCCESS = 200;
    int LOGOUT_SUCCESS = 200;
    int PASSWORD_INCORRECT = 202;
    int USERID_INCORRECT = 201;
    int ACTIVE_SESSION = 204;
}