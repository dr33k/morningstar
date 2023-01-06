package com.seven.ije.util;

import com.seven.ije.models.responses.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
}
