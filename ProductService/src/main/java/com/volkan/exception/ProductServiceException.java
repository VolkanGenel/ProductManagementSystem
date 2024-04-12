package com.volkan.exception;

import lombok.Getter;

@Getter
public class ProductServiceException extends RuntimeException{
    private final EErrorType errorType;

    public ProductServiceException(EErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public ProductServiceException(EErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }


}
