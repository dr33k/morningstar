package com.seven.ije.util;

import com.seven.ije.responses.Response;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Method;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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

    public static ResponseEntity <Response> notFound(String message) {
        return ResponseEntity.notFound().build();
    }
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

    public static ResponseEntity<Response> created(Set <? extends Record> records, String location) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path(location).buildAndExpand().toUri();
        return ResponseEntity.status(201).location(uri).body(
                Response.builder()
                .data(records)
                .isError(false)
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build());
    }
    public static EntityModel<ResponseEntity<Response>> okHal(Set <? extends Record> records){
        EntityModel<ResponseEntity<Response>> entityModel = EntityModel.of(
                ResponseEntity.ok(
                        Response.builder()
                                .data(records)
                                .isError(false)
                                .status(HttpStatus.OK)
                                .timestamp(LocalDateTime.now())
                                .build()
                )
        );
        return entityModel;
    }
    public static EntityModel<ResponseEntity<Response>> createdHal(Set <? extends Record> records, String location){
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path(location).buildAndExpand().toUri();

        EntityModel<ResponseEntity<Response>> entityModel = EntityModel.of(
                ResponseEntity.status(201).location(uri).body(
                Response.builder()
                        .data(records)
                        .isError(false)
                        .status(HttpStatus.CREATED)
                        .timestamp(LocalDateTime.now())
                        .build()
                )
        );
        return entityModel;
    }
    public static void createAndIncludeLinks(Map <String,Object> refToInvocationObjectMap ,EntityModel<ResponseEntity<Response>> entityModel) {
        //The key of an entryset in refToInvocationObjectMap is the "ref" property of the link
        //The value of an entrySet in refToInvocationObjectMap is the invocation object of type Object

        Set <Link> links = refToInvocationObjectMap.entrySet().stream().map(
                entry -> linkTo(entry.getValue()).withRel(entry.getKey())
        ).collect(Collectors.toSet());
        entityModel.add(links);
    }
}
