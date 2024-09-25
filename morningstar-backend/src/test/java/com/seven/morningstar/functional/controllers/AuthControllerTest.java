package com.seven.morningstar.functional.controllers;

import com.seven.morningstar.user_management.AuthController;
import com.seven.morningstar.user_management.UserCreateRequest;
import com.seven.morningstar.user_management.UserDTO;
import com.seven.morningstar.user_management.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static com.seven.morningstar.util.AppConstants.VERSION;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    UserService userService;
//    @Mock
//    AuthenticationProvider authenticationProvider;

    @InjectMocks
    AuthController authController;
    @BeforeEach
    public void init(){RestAssuredMockMvc.standaloneSetup(authController);}

    @Test
    void testShouldRegister(){
        UserCreateRequest request = new UserCreateRequest();
        request.setFirstName("Dreek");
        request.setLastName("Dreek");
        request.setEmail("dreek@seven.com");
        request.setPhoneNo("+23400000000000");
        request.setDateBirth(LocalDate.of(1000, 12,12));
        request.setPassword("!Woooooooo0");

        ArgumentCaptor<UserCreateRequest> captor = ArgumentCaptor.forClass(UserCreateRequest.class);

        Mockito.when(userService.register(captor.capture())).thenReturn(UserDTO.builder().build());

                RestAssuredMockMvc.given().contentType("application/json").body(request)
                .when().post(VERSION+"/register")
                .then().statusCode(201);

        Assertions.assertEquals(captor.getValue().getEmail(), request.getEmail() );
    }
    @Test
    void testShouldFailToRegister(){
        //Empty request fails validation
        UserCreateRequest request = new UserCreateRequest();

        RestAssuredMockMvc.given().contentType("application/json").body(request)
                .when().post(VERSION+"/register")
                .then().statusCode(400);
    }

//    @Test
//    void testShouldLogin(){
//        LoginRequest request = new LoginRequest();
//        request.setUsername("dreek@seven.com");
//        request.setPassword("!Woooooooo0");
//
//        ArgumentCaptor<UsernamePasswordAuthenticationToken> tokenCaptor = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
//
//        Mockito.verify(authenticationProvider).authenticate(tokenCaptor.capture());
//        Mockito.when(authenticationProvider.authenticate(tokenCaptor.getValue())).thenReturn(tokenCaptor.getValue());
//        User principal = (User) tokenCaptor.getValue().getPrincipal();
//        Mockito.when(userService.login(principal)).thenReturn(UserDTO.builder().build());
//
//        RestAssuredMockMvc.given().contentType("application/json").body(request)
//                .when().post(VERSION+"/login")
//                .then().statusCode(200);
//
//        Assertions.assertEquals(((User) tokenCaptor.getValue().getPrincipal()).getUsername(), request.getUsername());
//    }
}
