package com.seven.ije.controllers_advice;

import com.seven.ije.responses.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import static com.seven.ije.util.Responder.*;
@ControllerAdvice
public class AppExceptionHandler{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity <Response> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return badRequest(ex.getFieldError().getDefaultMessage());
    }
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity <Response> handleBadRequest(HttpClientErrorException.BadRequest ex) {
        return badRequest(ex.getMessage());
    }
    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity <Response> handleResponseStatusException(ResponseStatusException ex) {
        int status = ex.getStatusCode().value();
        switch(status) {
            case 400 -> {return badRequest(ex.getMessage());}
            case 404 -> {return notFound(ex.getMessage());}
            case 403 -> {return forbidden(ex.getMessage());}
            case 409 -> {return conflict(ex.getMessage());}
            case 500 -> {return internalServerError(ex.getMessage());}
            default ->  throw ex;
        }
    }
}
