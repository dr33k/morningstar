package com.seven.ije.models.exceptions;

public class PaymentException extends Exception {
    private Exception exception;
    public PaymentException(Exception exception){
        this.exception = exception;
    }

    @Override
    public String getMessage() {
        return exception.getMessage();
    }
    public Class<? extends Exception> getExceptionClass(){
        return exception.getClass();
    }
}
