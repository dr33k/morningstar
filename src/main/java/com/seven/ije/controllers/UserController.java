package com.seven.ije.controllers;

import com.seven.ije.models.records.UserRecord;
import com.seven.ije.models.requests.UserUpdateRequest;
import com.seven.ije.models.responses.Response;
import com.seven.ije.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Set;
import static com.seven.ije.util.Responder.ok;

@RestController
@RequestMapping("/user")
public class UserController {
    UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity <Response> getResource() {
        UserRecord userRecord = userService.get(null); //Signifies account owner access
        return ok(Set.of(userRecord));
    }

    @PutMapping("/update")
    public ResponseEntity <Response> updateResource(@Valid @RequestBody UserUpdateRequest request) {
        UserRecord userRecord = userService.update(request);
        return ok(Set.of(userRecord));
    }
    @DeleteMapping("/delete")
    public ResponseEntity <Response> deleteResource(@Valid @RequestParam(name = "email") String email) {
        userService.delete(email);
        return ok(Collections.emptySet());
    }
}