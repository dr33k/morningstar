package com.seven.ije.controllers;

import com.seven.ije.models.requests.UserCreateRequest;
import com.seven.ije.models.responses.Response;
import com.seven.ije.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Set;

import static com.seven.ije.util.Responder.ok;

@RestController
@RequestMapping("/register")
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
