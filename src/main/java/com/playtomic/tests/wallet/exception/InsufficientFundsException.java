package com.playtomic.tests.wallet.exception;

public class InsufficientFundsException extends RuntimeException {

    @Override
    public String getMessage(){
        return "Insufficient funds!";
    }
}
