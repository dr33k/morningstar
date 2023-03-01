package com.seven.ije.user_management;

import com.seven.ije.responses.Response;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;

import static com.seven.ije.util.Responder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

import static com.seven.ije.util.AppConstants.VERSION;


@RestController
@RequestMapping(VERSION+"/register")
public class RegistrationController {
    UserService userService;
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(produces = {"application/json" , "application/xml"}, consumes = {"application/json" , "application/xml"})
    public EntityModel<ResponseEntity<Response>> createResource(@Valid @RequestBody UserCreateRequest request) {
            Set records = Set.of(userService.create(request));

            EntityModel<ResponseEntity<Response>> entityModel = okHal(records);

            Map<String, Object> refToInvocationObjectMap = Map.of("view",methodOn(UserController.class).getResource());

            createAndIncludeLinks(refToInvocationObjectMap, entityModel);

            return entityModel;
    }
}
