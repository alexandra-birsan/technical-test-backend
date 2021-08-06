package com.playtomic.tests.wallet.exception;

public class StripeServiceException extends Exception {

    @Override
    public String getMessage(){
        return "Error while connecting to the payments platform!";
    }
}
