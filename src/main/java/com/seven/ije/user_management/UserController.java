package com.seven.ije.user_management;

import com.seven.ije.config.security.Authorize;
import com.seven.ije.responses.Response;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Set;

import static com.seven.ije.util.AppConstants.VERSION;
import static com.seven.ije.util.Responder.ok;
import static com.seven.ije.util.Responder.noContent;

@RestController
@RequestMapping(VERSION+"/user")
public class UserController {
    UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Authorize(roles = {"ROLE_ADMIN", "ADMIN"})
    public ResponseEntity <Response> getResource() {
        UserRecord userRecord = userService.get(); //Signifies account owner access
        return ok(userRecord);
    }

    @PutMapping("/update")
    public ResponseEntity <Response> updateResource(@Valid @RequestBody UserUpdateRequest request) {
        UserRecord userRecord = userService.update(request);
        return ok(userRecord);
    }
    @DeleteMapping("/delete")
    public ResponseEntity <Response> deleteResource(@Valid @RequestParam(name = "email") String email) {
        userService.delete(email);
        return noContent();
    }
}