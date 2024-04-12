package com.volkan.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum EErrorType {
    AUTH_NOT_FOUND(1003,"Auth is not registered", INTERNAL_SERVER_ERROR),
    REGISTER_ERROR_PASSWORD_UNMATCHED(1004, "Passwords are unmatched",BAD_REQUEST),
    REGISTER_ERROR_EMAIL(1005,"This email is already taken",BAD_REQUEST),
    LOGIN_ERROR_USERNAME_PASSWORD(1006,"Email or password is incorrect",BAD_REQUEST),
    INVALID_TOKEN(1007,"Invalid Token",BAD_REQUEST),
    METHOD_MIS_MATCH_ERROR(2002,"The format entered does not match the expected format.",BAD_REQUEST),
    METHOD_NOT_VALID_ARGUMENT_ERROR(2003,"Missing parameter submission in URL",BAD_REQUEST),
    EMAIL_NOT_FOUND(4001,"Email is not found please try again",HttpStatus.BAD_REQUEST),
    PASSWORD_UNMATCHED(4002,"Passwords are not matched",HttpStatus.BAD_REQUEST),
    LOGIN_ERROR(4003,"Email or password is incorrect",HttpStatus.BAD_REQUEST),
    STATUS_NOT_ACTIVE(4005,"Auth status is not active, please reset your password first to make it active",HttpStatus.BAD_REQUEST),
    AUTH_DELETED(4006,"Auth was deleted, please contact Manager!", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_CREATED(3001,"Token not created",HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER(3001,"Invalid parameter", BAD_REQUEST);

    private int code;
    private String message;
    private HttpStatus httpStatus;
}
