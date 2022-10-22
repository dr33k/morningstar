package com.seven.ije.controllers;

import com.seven.ije.config.security.UserAuthentication;
import com.seven.ije.models.entities.User;
import com.seven.ije.models.records.UserRecord;
import com.seven.ije.models.requests.UserUpdateRequest;
import com.seven.ije.models.responses.Response;
import com.seven.ije.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserAuthentication userAuthentication;
    @GetMapping("/search")
    public ResponseEntity<Response> getResource(@Valid @RequestParam(name = "id") String id) {
        //Check if user owns account
        User sender = (User) userAuthentication.getInstance().getPrincipal();

        if (sender.getEmail().equals(id)) {
            UserRecord userRecord = (UserRecord) userService.get(id);
            if (userRecord == null) {       //If resource was not found
                return ResponseEntity.status(404).body(Response.builder()
                        .isError(true)
                        .message("User is not available")
                        .status(HttpStatus.NOT_FOUND)
                        .timestamp(LocalDateTime.now())
                        .build());
            } else if (!(userRecord.message() == null)) {   //If there was an error during the process
                return ResponseEntity.status(404).body(Response.builder()
                        .isError(true)
                        .message(userRecord.message())
                        .status(HttpStatus.NOT_FOUND)
                        .timestamp(LocalDateTime.now())
                        .build());
            } else {
                return ResponseEntity.ok(Response.builder()
                        .data(Set.of(userRecord))
                        .isError(false)
                        .status(HttpStatus.FOUND)
                        .timestamp(LocalDateTime.now())
                        .build());
            }
        } else {
            return ResponseEntity.status(403).body(Response.builder()
                    .isError(true)
                    .message("You do not own this account")
                    .status(HttpStatus.FORBIDDEN)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateResource(@Valid @RequestBody UserUpdateRequest request) {
        //Check if user owns account
        User sender = (User) userAuthentication.getInstance().getPrincipal();
        UserRecord userRecord = UserRecord.copy(request);
        userRecord = (UserRecord) userService.get(userRecord.email());

        if (sender.getEmail().equals(userRecord.email())) {
            userRecord = (UserRecord) userService.update(userRecord);
            if (userRecord == null) {       //If resource was not found
                return ResponseEntity.of(Optional.of(Response.builder()
                        .isError(true)
                        .message("A couple of things you might try checking:" +
                                "\n*Make sure the id was entered correctly" +
                                "\n*Make sure you are not trying to update with the same information as before")
                        .status(HttpStatus.NOT_MODIFIED)
                        .timestamp(LocalDateTime.now())
                        .build()));
            } else if (!(userRecord.message() == null)) {   //If there was an error during the process
                return ResponseEntity.of(Optional.of(Response.builder()
                        .isError(true)
                        .message(userRecord.message())
                        .status(HttpStatus.NOT_MODIFIED)
                        .timestamp(LocalDateTime.now())
                        .build()));
            } else {
                return ResponseEntity.ok(Response.builder()
                        .data(Set.of(userRecord))
                        .isError(false)
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now())
                        .build());
            }
        }
        else{
                return ResponseEntity.status(403).body(Response.builder()
                        .isError(true)
                        .message("You do not own this account")
                        .status(HttpStatus.FORBIDDEN)
                        .timestamp(LocalDateTime.now())
                        .build());
            }
        }

    public ResponseEntity<Response> deleteResource ( String id){
            //Check if user owns account
            User sender = (User) userAuthentication.getInstance().getPrincipal();

            if (sender.getEmail().equals(id)) {
            Boolean deleted = userService.delete(id);
            return (deleted) ?
                    ResponseEntity.ok(Response.builder()
                            .message("User: " + id + " deleted successfully")
                            .isError(false)
                            .status(HttpStatus.OK)
                            .timestamp(LocalDateTime.now())
                            .build())
                    :
                    ResponseEntity.status(404).body(Response.builder()
                            .isError(true)
                            .message("User not found")
                            .status(HttpStatus.NOT_FOUND)
                            .timestamp(LocalDateTime.now())
                            .build());
        } else{
        return ResponseEntity.status(403).body(Response.builder()
                .isError(true)
                .message("You do not own this account")
                .status(HttpStatus.FORBIDDEN)
                .timestamp(LocalDateTime.now())
                .build());
    }
    }
    }