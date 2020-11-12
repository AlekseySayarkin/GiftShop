package com.epam.esm.exception;

import com.epam.esm.dao.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class WebException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;

    public WebException(ErrorCode errorCode, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public WebException(String message, ErrorCode errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public WebException(String message, Throwable cause, ErrorCode errorCode, HttpStatus httpStatus) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public WebException(Throwable cause, ErrorCode errorCode, HttpStatus httpStatus) {
        super(cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
