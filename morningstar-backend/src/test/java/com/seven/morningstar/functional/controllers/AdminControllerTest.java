package com.seven.morningstar.functional.controllers;

import com.seven.morningstar.backend.booking.BookingService;
import com.seven.morningstar.backend.ticket.TicketService;
import com.seven.morningstar.backend.user_management.AdminController;
import com.seven.morningstar.backend.user_management.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import static com.seven.morningstar.backend.util.AppConstants.VERSION;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {
    @Mock TicketService ticketService;
    @Mock UserService userService;
    @Mock BookingService bookingService;

    @InjectMocks
    AdminController adminController;

    @BeforeEach
    void init(){RestAssuredMockMvc.standaloneSetup(adminController);}

    @Test
    void testShouldGetTicket(){
        UUID bId = UUID.randomUUID();

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        Mockito.when(ticketService.get(captor.capture())).thenReturn(null);

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .param("bookingNo", bId)
                .when().get(VERSION+"/administrator/tickets/search")
                .then().statusCode(200);

        Assertions.assertEquals(captor.getValue(), bId);
    }
    @Test
    void testShouldFailToGetTicket(){
        //Invalid id
        UUID bId = null;

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .param("bookingNo", bId)
                .when().get(VERSION+"/administrator/tickets/search")
                .then().statusCode(400);
    }
    @Test
    void testShouldGetUser(){
        String id = "dreek@seven.com";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        Mockito.when(userService.get(captor.capture())).thenReturn(null);

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .param("id", id)
                .when().get(VERSION+"/administrator/users/search")
                .then().statusCode(200);

        Assertions.assertEquals(captor.getValue(), id);
    }
    @Test
    @Disabled
    void testShouldFailToGetUser(){
        //Invalid id
        String id = null;

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .param("id", id)
                .when().get(VERSION+"/administrator/users/search")
                .then().statusCode(400);
    }

}
