package com.seven.morningstar.security;

import com.seven.morningstar.config.security.BCryptEncoder;
import com.seven.morningstar.config.security.JwtAuthenticationFilter;
import com.seven.morningstar.config.security.JwtService;
import com.seven.morningstar.config.security.SecurityConfigurer;
import com.seven.morningstar.enums.StateCode;
import com.seven.morningstar.location.LocationController;
import com.seven.morningstar.location.LocationCreateRequest;
import com.seven.morningstar.location.LocationService;
import com.seven.morningstar.user_management.UserService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import static com.seven.morningstar.constants.TestConstants.*;
import static com.seven.morningstar.util.AppConstants.VERSION;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.webAppContextSetup;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = LocationController.class)
@Import({SecurityConfigurer.class, BCryptEncoder.class, JwtAuthenticationFilter.class})
@ActiveProfiles("test")
public class LocationControllerSecurityTest {
    @MockBean
    LocationService locationService;
    @MockBean
    UserService userService;
    @MockBean
    JwtService jwtService;
    @Autowired
    WebApplicationContext context;
    @BeforeEach
    void init(){
        webAppContextSetup(context);
        Mockito.when(jwtService.isTokenValid(ADMINCLAIMS)).thenReturn(true);
        Mockito.when(userService.loadUserByUsername(Mockito.anyString())).thenReturn(ADMIN);
    }

    @Test
    void testShouldCreateLocationIfAdminUser(){
        final String STATION_NAME = "My First Abia Station";
        LocationCreateRequest request = new LocationCreateRequest();
        request.setStateCode(StateCode.ABIA);
        request.setStationName(STATION_NAME);

        Mockito.when(jwtService.extractClaims(Mockito.anyString())).thenReturn(ADMINCLAIMS);
        Mockito.when(locationService.create(request)).thenReturn(null);

        given()
                .header("Authorization", TOKEN)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(VERSION+"/administrator/location/create")
                .then().statusCode(201);
    }
    @Test
    void testShouldForbidCreateLocationIfNotAdminUser(){
        final String STATION_NAME = "My First Abia Station";
        LocationCreateRequest request = new LocationCreateRequest();
        request.setStateCode(StateCode.ABIA);
        request.setStationName(STATION_NAME);

        Mockito.when(jwtService.extractClaims(Mockito.anyString())).thenReturn(USERCLAIMS);
        Mockito.when(locationService.create(request)).thenReturn(null);

        given()
                .header("Authorization", TOKEN)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(VERSION+"/administrator/location/create")
                .then().statusCode(403);
    }
}
