package com.volkan.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum EErrorType {
    AUTH_NOT_FOUND(404,"Auth is not found", NOT_FOUND),
    REGISTER_ERROR_PASSWORD_UNMATCHED(400, "Passwords are unmatched",BAD_REQUEST),
    REGISTER_ERROR_EMAIL(400,"This email is already taken",BAD_REQUEST),
    LOGIN_ERROR_USERNAME_PASSWORD(400,"Email or password is incorrect",BAD_REQUEST),
    INVALID_TOKEN(403,"Invalid Token",FORBIDDEN),
    METHOD_MIS_MATCH_ERROR(400,"The format entered does not match the expected format.",BAD_REQUEST),
    METHOD_NOT_VALID_ARGUMENT_ERROR(400,"Missing parameter submission in URL",BAD_REQUEST),
    EMAIL_NOT_FOUND(404,"No auth found with this email",NOT_FOUND),
    PASSWORD_UNMATCHED(400,"Passwords are not matched",BAD_REQUEST),
    LOGIN_ERROR(400,"Email or password is incorrect",BAD_REQUEST),
    STATUS_NOT_ACTIVE(401,"Auth status is not active, please reset your password first to make it active",UNAUTHORIZED),
    AUTH_DELETED(403,"Auth was deleted, please contact Manager!", FORBIDDEN),
    TOKEN_NOT_CREATED(400,"Token not created",BAD_REQUEST),
    INVALID_PARAMETER(400,"Invalid parameter", BAD_REQUEST),
    UNAUTHORIZED_REQUEST(401,"Unauthorized request",UNAUTHORIZED);


    private int code;
    private String message;
    private HttpStatus httpStatus;
}
