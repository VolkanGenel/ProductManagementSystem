package com.volkan.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;

import static org.springframework.http.HttpStatus.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum EErrorType {
    PRODUCT_NOT_FOUND(404,"The product cannot be found", NOT_FOUND),
    INVALID_TOKEN(403,"Invalid Token",FORBIDDEN),
    METHOD_MIS_MATCH_ERROR(400,"The format entered does not match the expected format.",BAD_REQUEST),
    METHOD_NOT_VALID_ARGUMENT_ERROR(400,"Missing parameter submission in URL",BAD_REQUEST),
    INVALID_PARAMETER(400,"Invalid Parameter Error", BAD_REQUEST),
    STATUS_NOT_ACTIVE(403,"Auth status is not active, please make it active",FORBIDDEN),
    NO_TOKEN_PRESENTED(401,"Please login first and provide a valid token",UNAUTHORIZED),
    NO_PERMISSION(403,"Tou don't have permission to do process",FORBIDDEN),
    TOO_MANY_REQUESTS(429,"Too many try please try again one minute later", HttpStatus.TOO_MANY_REQUESTS);

    private int code;
    private String message;
    private HttpStatus httpStatus;
}
