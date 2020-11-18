package com.epam.esm.dao.exception;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCodeEnum {

    FAILED_TO_RETRIEVE_CERTIFICATE(50201),
    FAILED_TO_DELETE_CERTIFICATE(50301),
    FAILED_TO_UPDATE_CERTIFICATE(50401),
    FAILED_TO_ADD_CERTIFICATE(50501),
    FAILED_TO_RETRIEVE_TAG(50602),
    FAILED_TO_DELETE_TAG(50702),
    FAILED_TO_ADD_TAG(50802),
    INVALID_INPUT(509),
    INVALID_SORT_INPUT(510),
    CERTIFICATE_VALIDATION_ERROR(51100),
    TAG_VALIDATION_ERROR(51201);

    private final int code;

    ErrorCodeEnum(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }
}
