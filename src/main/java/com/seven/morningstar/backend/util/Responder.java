package com.seven.morningstar.backend.util;

import com.seven.morningstar.backend.responses.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

public final class Responder {
    public static ResponseEntity <Response> ok(Object records) {
        return ResponseEntity.ok(Response.builder()
                .data(records)
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build());
    }
    public static ResponseEntity <Response> ok(Object records, String token) {
        return ResponseEntity.ok(Response.builder()
                .token(token)
                .data(records)
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build());
    }

    public static ResponseEntity <Response> badRequest(String message) {
        return ResponseEntity.status(400).body(Response.builder()
                .message(message)
                .isError(true)
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build());
    }

    public static ResponseEntity <Response> notFound(String message) {
        return ResponseEntity.notFound().build();
    }
    public static ResponseEntity <Response> noContent() {return ResponseEntity.noContent().build();}
    public static ResponseEntity <Response> forbidden(String message) {
        return ResponseEntity.status(403).body(Response.builder()
                .message(message)
                .isError(true)
                .status(HttpStatus.FORBIDDEN)
                .timestamp(LocalDateTime.now())
                .build());
    }

    public static ResponseEntity <Response> conflict(String message) {
        return ResponseEntity.status(409).body(Response.builder()
                .message(message)
                .isError(true)
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build());
    }
    public static ResponseEntity <Response> internalServerError(String message) {
        return ResponseEntity.internalServerError().body(Response.builder()
                .message(message)
                .isError(true)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .build());
    }

    public static ResponseEntity<Response> created(Object records, String location) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path(location).buildAndExpand().toUri();
        return ResponseEntity.status(201).location(uri).body(
                Response.builder()
                .data(records)
                .isError(false)
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
