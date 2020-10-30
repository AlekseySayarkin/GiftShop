package com.epam.esm.dao.exception;

public class PersistenceException extends Exception {

    private final ErrorCode errorCode;

    public PersistenceException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public PersistenceException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public PersistenceException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public PersistenceException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
