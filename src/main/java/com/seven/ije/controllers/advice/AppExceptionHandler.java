package com.seven.ije.controllers.advice;

import com.seven.ije.models.responses.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.seven.ije.util.Responder.badRequest;
@ControllerAdvice
public class AppExceptionHandler{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity <Response> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return badRequest(ex.getFieldError().toString());
    }
}
