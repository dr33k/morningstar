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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
    UserService userService;

    @GetMapping
    public ResponseEntity <Response> getResource() {
        UserRecord userRecord = (UserRecord) userService.get(null); //Signifies account owner access

        return ResponseEntity.ok(Response.builder()
                .data(Set.of(userRecord))
                .isError(false)
                .status(HttpStatus.FOUND)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/update")
    public ResponseEntity <Response> updateResource(@Valid @RequestBody UserUpdateRequest request) {
        UserRecord userRecord = (UserRecord) userService.update(request);

        return ResponseEntity.ok(Response.builder()
                .data(Set.of(userRecord))
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build());
    }
    @DeleteMapping("/delete")
    public ResponseEntity <Response> deleteResource(@Valid @RequestParam(name = "email") String email) {
        userService.delete(email);
        return ResponseEntity.ok(Response.builder()
                .message("User: " + email + " deleted successfully")
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build());
    }
}