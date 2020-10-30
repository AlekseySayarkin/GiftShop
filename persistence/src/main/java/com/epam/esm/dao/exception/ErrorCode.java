package com.epam.esm.dao.exception;

public enum ErrorCode {

    NOT_FOUND(404), FAILED_TO_ADD(501);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getErrorCode() {
        return code;
    }
}
