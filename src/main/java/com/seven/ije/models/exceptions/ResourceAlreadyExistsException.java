package com.seven.ije.models.exceptions;

public class ResourceAlreadyExistsException extends Exception{
    private String message;
    public ResourceAlreadyExistsException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
