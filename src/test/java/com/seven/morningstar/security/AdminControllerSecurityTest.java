package com.seven.morningstar.security;

import com.seven.morningstar.booking.BookingService;
import com.seven.morningstar.config.security.BCryptEncoder;
import com.seven.morningstar.config.security.JwtAuthenticationFilter;
import com.seven.morningstar.config.security.JwtService;
import com.seven.morningstar.config.security.SecurityConfigurer;
import com.seven.morningstar.ticket.TicketService;
import com.seven.morningstar.user_management.AdminController;
import com.seven.morningstar.user_management.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.context.WebApplicationContext;

import static com.seven.morningstar.util.AppConstants.VERSION;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AdminController.class)
@Import({SecurityConfigurer.class, BCryptEncoder.class, JwtAuthenticationFilter.class, JwtService.class})

public class AdminControllerSecurityTest {

    @MockBean TicketService ticketService;
    @MockBean BookingService bookingService;
    @MockBean UserService userService;
    @Autowired
    WebApplicationContext context;


    @BeforeEach
    void init(){
        RestAssuredMockMvc.webAppContextSetup(context);
    }

    @Test
    @WithMockUser(roles = {"PASSENGER", "OFFICER"})
    void testShouldForbidNonAdminsFromTickets(){
        RestAssuredMockMvc.given().contentType("application/json")
                .when().get(VERSION+"/administrator/tickets")
                .then().statusCode(403);
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void testShouldPermitAdminsToTickets(){
        RestAssuredMockMvc.given().contentType("application/json")
                .when().get(VERSION+"/administrator/tickets")
                .then().statusCode(200);
    }
    @Test
    @WithMockUser(roles = {"PASSENGER", "OFFICER"})
    void testShouldForbidNonAdminsFromBookings(){
        RestAssuredMockMvc.given().contentType("application/json")
                .when().get(VERSION+"/administrator/bookings")
                .then().statusCode(403);
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void testShouldPermitAdminsToBookings(){
        RestAssuredMockMvc.given().contentType("application/json")
                .when().get(VERSION+"/administrator/bookings")
                .then().statusCode(200);
    }
    @Test
    @WithMockUser(roles = {"PASSENGER", "OFFICER"})
    void testShouldForbidNonAdminsFromUsers(){
        RestAssuredMockMvc.given().contentType("application/json")
                .when().get(VERSION+"/administrator/users")
                .then().statusCode(403);
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void testShouldPermitAdminsToUsers(){
        RestAssuredMockMvc.given().contentType("application/json")
                .when().get(VERSION+"/administrator/users")
                .then().statusCode(200);
    }

}
