package com.seven.ije.controllers;

import com.seven.ije.models.exceptions.ResourceAlreadyExistsException;
import com.seven.ije.models.records.UserRecord;
import com.seven.ije.models.requests.UserCreateRequest;
import com.seven.ije.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@RequestMapping("/register")
public class RegistrationController{
    @Autowired
    UserService userService;

    @PostMapping
    ModelAndView createResource(@Valid @RequestBody UserCreateRequest request) throws ResourceAlreadyExistsException{
        UserRecord userRecord = UserRecord.copy(request);
        userRecord = (UserRecord) userService.create(userRecord);

        return new ModelAndView("redirect:/dashboard","userRecord",userRecord);
    }
}
