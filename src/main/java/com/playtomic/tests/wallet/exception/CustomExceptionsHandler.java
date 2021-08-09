package com.playtomic.tests.wallet.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {StripeServiceException.class})
    protected ResponseEntity<Object> handleWebClientResponseException(StripeServiceException ex) {
        return new ResponseEntity<>(new AppError(ex.getMessage()), createHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {InsufficientFundsException.class})
    protected ResponseEntity<Object> handleWebClientResponseException(InsufficientFundsException ex) {
        return new ResponseEntity<>(new AppError(ex.getMessage()), createHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {WalletNotFoundException.class})
    protected ResponseEntity<Object> handleWebClientResponseException(WalletNotFoundException ex) {
        return new ResponseEntity<>(new AppError(ex.getMessage()), createHeaders(), HttpStatus.NOT_FOUND);

    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
