package com.seven.ije.controllers;

import com.seven.ije.models.exceptions.ArbitraryException;
import com.seven.ije.models.exceptions.PaymentException;
import com.seven.ije.models.exceptions.ResourceAlreadyExistsException;
import com.seven.ije.models.responses.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import static org.springframework.web.client.HttpClientErrorException.*;

@ControllerAdvice
public class ExceptionController {

    //HTTP_CLIENT_ERROR EXCEPTIONS
    @ExceptionHandler(value = NotFound.class)
    protected ResponseEntity<Object> notFoundHandler(NotFound e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.builder()
                .isError(true)
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = BadRequest.class)
    protected ResponseEntity<Response> badRequestHandler(BadRequest e){
        return ResponseEntity.badRequest().body(Response.builder()
                .isError(true)
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build());
    }
//CUSTOM EXCEPTIONS
    @ExceptionHandler(value = ResourceAlreadyExistsException.class)
    protected ResponseEntity<Response> resourceAlreadyExistsHandler(ResourceAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Response.builder()
                .isError(true)
                .message(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = PaymentException.class)
    protected ResponseEntity<Response> paymentHandler(PaymentException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.builder()
                .isError(true)
                .message("Payment processing failed, please try again\n"+e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(value = ArbitraryException.class)
    protected ResponseEntity<Response>arbitraryExceptionHandler(ArbitraryException e){
        return ResponseEntity.internalServerError().body(Response.builder()
                .isError(true)
                .message(e.getClass().toGenericString()+" : "+e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .build());
    }

    //ALL OTHERS
    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity<Response> internalServerErrorHandler(RuntimeException e){
        return ResponseEntity.internalServerError().body(Response.builder()
                .isError(true)
                .message(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
