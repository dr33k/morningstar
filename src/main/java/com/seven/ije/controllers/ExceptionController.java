package com.seven.ije.controllers;

import com.seven.ije.models.exceptions.PaymentException;
import com.seven.ije.models.exceptions.ResourceAlreadyExistsException;
import com.seven.ije.models.responses.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ResourceAccessException.class)
    protected ResponseEntity<Object> notFoundHandler(ResourceAccessException e, WebRequest request){

        return handleExceptionInternal(e, e.getMessage(),
                new HttpHeaders(),HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity<Response> badRequestHandler(RuntimeException e){
        return ResponseEntity.badRequest().body(Response.builder()
                .isError(true)
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = ResourceAlreadyExistsException.class)
    protected ResponseEntity<Response> existsHandler(ResourceAlreadyExistsException e){
        return ResponseEntity.badRequest().body(Response.builder()
                .isError(true)
                .message(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = PaymentException.class)
    protected ResponseEntity<Response> paymentHandler(PaymentException e){
        return ResponseEntity.status(402).body(Response.builder()
                .isError(true)
                .message("Payment processing failed, please try again\n"+e.getMessage())
                .status(HttpStatus.valueOf(402))
                .timestamp(LocalDateTime.now())
                .build());
    }
}
