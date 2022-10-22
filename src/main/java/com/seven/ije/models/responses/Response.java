package com.seven.ije.models.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@JsonInclude
public class Response {
    private Set<? extends Record> data;
    private HttpStatus status;
    private Boolean isError;
    private String message;
    private LocalDateTime timestamp;
}