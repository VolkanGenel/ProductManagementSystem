package com.volkan.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    private ErrorMessage createErrorMessage(EErrorType errorType, Exception exception) {
        System.out.println("Error occurred....: "+ exception.getMessage());
        return ErrorMessage.builder()
                .code(errorType.getCode())
                .message(errorType.getMessage())
                .build();
    }
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ApiResponses
    public ResponseEntity<String> handleException(Exception exception) {
        System.out.println("Undefined error has occurred.....: "+exception.getMessage());
        return ResponseEntity.badRequest().body("An unexpected error has occurred "+exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ProductServiceException.class)
    public ResponseEntity<ErrorMessage> handleProductManagerException(ProductServiceException exception) {
        EErrorType errorType = exception.getErrorType();
        HttpStatus httpStatus = errorType.getHttpStatus();
        return new ResponseEntity<>(createErrorMessage(errorType,exception),httpStatus);
    }
    @ResponseBody
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorMessage> handleInvalidFormatException(InvalidFormatException exception) {
        EErrorType errorType = EErrorType.INVALID_PARAMETER;
        return new ResponseEntity<>(createErrorMessage(errorType,exception),errorType.getHttpStatus());
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handle(MethodArgumentTypeMismatchException exception) {
        EErrorType errorType = EErrorType.METHOD_MIS_MATCH_ERROR;
        return new ResponseEntity<>(createErrorMessage(errorType,exception),errorType.getHttpStatus());
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handle(HttpMessageNotReadableException exception) {
        EErrorType errorType = EErrorType.METHOD_MIS_MATCH_ERROR;
        return new ResponseEntity<>(createErrorMessage(errorType,exception),errorType.getHttpStatus());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handle(MethodArgumentNotValidException exception) {
        EErrorType errorType = EErrorType.METHOD_NOT_VALID_ARGUMENT_ERROR;
        return new ResponseEntity<>(createErrorMessage(errorType,exception),errorType.getHttpStatus());
    }
    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ErrorMessage> handleSpecificException(ServletException exception) {
        EErrorType errorType = EErrorType.TOO_MANY_REQUESTS;
        return new ResponseEntity<>(createErrorMessage(errorType,exception),errorType.getHttpStatus());
    }
    @ExceptionHandler(AuthorizationServiceException.class)
    public ResponseEntity<ErrorMessage> handleSpecificException(AuthorizationServiceException exception) {
        EErrorType errorType = EErrorType.INVALID_TOKEN;
        return new ResponseEntity<>(createErrorMessage(errorType,exception),errorType.getHttpStatus());
    }
}
