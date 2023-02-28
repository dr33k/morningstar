package com.seven.ije.user_management;

import com.seven.ije.user_management.UserCreateRequest;
import com.seven.ije.responses.Response;
import com.seven.ije.user_management.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static com.seven.ije.util.AppConstants.VERSION;
import static com.seven.ije.util.Responder.ok;

@RestController
@RequestMapping(VERSION+"/register")
public class RegistrationController{
    UserService userService;
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public ResponseEntity<Response> createResource(@Valid @RequestBody UserCreateRequest request){
        return ok(Set.of(userService.create(request)));
    }
}
