package com.seven.ije.util;

import com.seven.ije.models.responses.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Set;

public final class Responder {
    public static ResponseEntity <Response> ok(Set <? extends Record> records) {
        return ResponseEntity.ok(Response.builder()
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

    public static ResponseEntity <Response> created(Set <? extends Record> records, String location) {
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
