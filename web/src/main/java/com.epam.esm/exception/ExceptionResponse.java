package com.epam.esm.exception;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {

    private final HttpStatus httpStatus;
    private final String message;
    private final int errorCode;

    public ExceptionResponse(HttpStatus httpStatus, String message, int errorCode) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
