package com.epam.esm.controller;

import com.epam.esm.exception.ExceptionResponse;
import com.epam.esm.exception.WebException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(WebException.class)
    public ResponseEntity<ExceptionResponse> handleServiceException(WebException exception) {
        var response = new ExceptionResponse(exception.getHttpStatus(),
                exception.getMessage(), exception.getErrorCode().getErrorCode());
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
