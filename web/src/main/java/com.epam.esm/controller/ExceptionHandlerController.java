package com.epam.esm.controller;

import com.epam.esm.exception.WebException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(WebException.class)
    public ResponseEntity<WebException> handleServiceException(WebException exception) {
        return new ResponseEntity<>(exception, exception.getHttpStatus());
    }
}
