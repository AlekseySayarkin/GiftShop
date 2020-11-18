package com.epam.esm.dao.exception;

public enum ErrorCodeEnum {

    FAILED_TO_RETRIEVE_CERTIFICATE(502),
    FAILED_TO_DELETE_CERTIFICATE(503),
    FAILED_TO_UPDATE_CERTIFICATE(504),
    FAILED_TO_ADD_CERTIFICATE(505),
    FAILED_TO_RETRIEVE_TAG(506),
    FAILED_TO_DELETE_TAG(507),
    FAILED_TO_ADD_TAG(508),
    INVALID_INPUT(509),
    INVALID_SORT_INPUT(510),
    CERTIFICATE_VALIDATION_ERROR(511),
    TAG_VALIDATION_ERROR(512);

    private final int code;

    ErrorCodeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
