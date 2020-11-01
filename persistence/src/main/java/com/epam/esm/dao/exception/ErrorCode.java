package com.epam.esm.dao.exception;

public enum ErrorCode {

    NOT_FOUND(404), FAILED_TO_ADD(501), FAILED_TO_UPDATE(502), FAILED_TO_DELETE(506);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getErrorCode() {
        return code;
    }
}
