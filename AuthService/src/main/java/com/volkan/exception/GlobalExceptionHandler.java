package com.volkan.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
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
    public ResponseEntity<String> handleException(Exception exception) {
        System.out.println("Undetected error occurred.....: "+exception.getMessage());
        return ResponseEntity.badRequest().body("An unexpected error occurred in the application"+exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(AuthServiceException.class)
    public ResponseEntity<ErrorMessage> handleAuthException(AuthServiceException exception) {
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
    public final ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        EErrorType errorType = EErrorType.INVALID_PARAMETER;
        List<String> fields = new ArrayList<>();
        exception.getBindingResult().getFieldErrors().forEach(e -> fields.add(e.getField() + ": " + e.getDefaultMessage()));
        ErrorMessage errorMessage = createErrorMessage(errorType, exception);
        errorMessage.setFields(fields);
        return new ResponseEntity<>(errorMessage, errorType.getHttpStatus());
    }
}
