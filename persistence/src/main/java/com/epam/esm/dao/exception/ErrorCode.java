package com.epam.esm.dao.exception;

public class ErrorCode {

    private final int code;

    public ErrorCode(int code) {
        this.code = code;
    }

    public int getErrorCode() {
        return code;
    }
}
