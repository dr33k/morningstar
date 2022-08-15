package com.seven.RailroadApp.controllers;

import com.seven.RailroadApp.models.entities.LocationId;
import com.seven.RailroadApp.models.records.UserRecord;
import com.seven.RailroadApp.models.requests.LocationRequest;
import com.seven.RailroadApp.models.requests.UserRequest;
import com.seven.RailroadApp.models.responses.Response;
import com.seven.RailroadApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/register")
public class RegistrationController{
    @Autowired
    UserService userService;

    @PostMapping
    ResponseEntity<Response> createResource(@Valid @RequestBody UserRequest request){
        UserRecord userRecord = UserRecord.copy(request);
        userRecord = (UserRecord) userService.create(userRecord);
        return (!(userRecord.message() == null))?
                ResponseEntity.badRequest().body(Response.builder()
                        .isError(true)
                        .message(userRecord.message())
                        .status(HttpStatus.BAD_REQUEST)
                        .timestamp(LocalDateTime.now())
                        .build())
                :
                ResponseEntity.created(URI.create("/dashboard")).body(Response.builder()
                        .data(Set.of(userRecord))
                        .isError(false)
                        .status(HttpStatus.CREATED)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
