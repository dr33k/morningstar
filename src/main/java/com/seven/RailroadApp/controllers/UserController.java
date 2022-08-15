package com.seven.RailroadApp.controllers;

import com.seven.RailroadApp.models.records.UserRecord;
import com.seven.RailroadApp.models.requests.UserUpdateRequest;
import com.seven.RailroadApp.models.responses.Response;
import com.seven.RailroadApp.services.UserService;
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
public class UserController implements Controller<UserUpdateRequest,String>{
    @Autowired
    UserService userService;
    
    @Override
    public ResponseEntity<Response> getAllResources() {
        Set<UserRecord> userRecords = userService.getAll();
        return ResponseEntity.ok(Response.builder()
                .data(userRecords)
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @Override
    public ResponseEntity<Response> getResource(@RequestParam(name="email") String email) {
        UserRecord userRecord = (UserRecord) userService.get(email);
        if(userRecord == null) {       //If resource was not found
            return ResponseEntity.status(404).body(Response.builder()
                    .isError(true)
                    .message("User is not available")
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
        else if(!(userRecord.message() == null)) {   //If there was an error during the process
            return ResponseEntity.status(404).body(Response.builder()
                    .isError(true)
                    .message(userRecord.message())
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
        else {
            return ResponseEntity.ok(Response.builder()
                    .data(Set.of(userRecord))
                    .isError(false)
                    .status(HttpStatus.FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @Override
    public ResponseEntity<Response> createResource(UserUpdateRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Response> updateResource(@Valid @RequestBody UserUpdateRequest request) {
        UserRecord userRecord = UserRecord.copy(request);
        userRecord = (UserRecord) userService.update(userRecord);
        if(userRecord == null) {       //If resource was not found
            return ResponseEntity.of(Optional.of(Response.builder()
                    .isError(true)
                    .message("A couple of things you might try checking:" +
                             "\n*Make sure the id was entered correctly" +
                             "\n*Make sure you are not trying to update with the same information as before")
                    .status(HttpStatus.NOT_MODIFIED)
                    .timestamp(LocalDateTime.now())
                    .build()));
        }
        else if(!(userRecord.message() == null)) {   //If there was an error during the process
            return ResponseEntity.of(Optional.of(Response.builder()
                    .isError(true)
                    .message(userRecord.message())
                    .status(HttpStatus.NOT_MODIFIED)
                    .timestamp(LocalDateTime.now())
                    .build()));
        }
        else {
            return ResponseEntity.ok(Response.builder()
                    .data(Set.of(userRecord))
                    .isError(false)
                    .status(HttpStatus.OK)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @Override
    public ResponseEntity<Response> deleteResource(@RequestParam(name="email") String email) {
        Boolean deleted = userService.delete(email);
        return (deleted)?
                ResponseEntity.ok(Response.builder()
                        .message("User: "+email+" deleted successfully")
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
    }
}