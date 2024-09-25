package com.seven.morningstar.functional.controllers;

import com.seven.morningstar.backend.location.enums.LocationStatus;
import com.seven.morningstar.backend.location.enums.StateCode;
import com.seven.morningstar.backend.location.*;
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

import java.util.Collections;

import static com.seven.morningstar.backend.util.AppConstants.VERSION;

@ExtendWith(MockitoExtension.class)
public class LocationControllerTest {
    @Mock
    LocationService locationService;
    @InjectMocks
    LocationController locationController;
    
    @BeforeEach
    void init(){RestAssuredMockMvc.standaloneSetup(locationController);}

    @Test
    void testShouldGetAllLocations(){
        Mockito.when(locationService.getAll()).thenReturn(Collections.emptySet());
        RestAssuredMockMvc.given().contentType("application/json")
                .when().get(VERSION+"/location").then().statusCode(200);
    }

    @Test
    void testShouldUpdateLocation(){
        LocationId id = new LocationId(StateCode.ABUJ, "01");

        LocationUpdateRequest request = new LocationUpdateRequest();
        request.setLocationId(id);
        request.setStatus(LocationStatus.USED);

        ArgumentCaptor<LocationUpdateRequest> captor1 = ArgumentCaptor.forClass(LocationUpdateRequest.class);
        Mockito.when(locationService.update(captor1.capture())).thenReturn(null);

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .param("stateCode", id.getStateCode())
                .param("stationNo", id.getStationNo())
                .body(request)
                .when().put(VERSION+"/location/update")
                .then().statusCode(200);

        Assertions.assertEquals(captor1.getValue().getLocationId(), id);
        Assertions.assertEquals(captor1.getValue().getStatus(), LocationStatus.USED);

    }

    @Test
    void testShouldCreateLocation(){
        LocationCreateRequest request = new LocationCreateRequest();
        request.setStateCode(StateCode.ABUJ);
        request.setStationName("My Station");

        ArgumentCaptor<LocationCreateRequest> captor1 = ArgumentCaptor.forClass(LocationCreateRequest.class);
        Mockito.when(locationService.create(captor1.capture())).thenReturn(null);

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when().post(VERSION+"/location/create")
                .then().statusCode(201);

        Assertions.assertEquals(captor1.getValue().getStationName(), "My Station");
    }
    @Test
    void testShouldFailToCreateLocation(){
        //Empty request
        LocationCreateRequest request = new LocationCreateRequest();

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when().post(VERSION+"/location/create")
                .then().statusCode(400);
    }

    @Test
    void testShouldGetLocation(){
        LocationId id = new LocationId(StateCode.ABUJ, "01");

        ArgumentCaptor<LocationId> captor1 = ArgumentCaptor.forClass(LocationId.class);
        Mockito.when(locationService.get(captor1.capture())).thenReturn(null);

        RestAssuredMockMvc.given()
                .param("stateCode", id.getStateCode())
                .param("stationNo", id.getStationNo())
                .contentType("application/json")
                .when().get(VERSION+"/location/search")
                .then().statusCode(200);

        Assertions.assertEquals(captor1.getValue(), id);

    }
    @Test
    void testShouldDeleteLocation(){
        LocationId id = new LocationId(StateCode.ABUJ, "01");

        ArgumentCaptor<LocationId> captor1 = ArgumentCaptor.forClass(LocationId.class);
        Mockito.doNothing().when(locationService).delete(captor1.capture());

        RestAssuredMockMvc.given()
                .param("stateCode", id.getStateCode())
                .param("stationNo", id.getStationNo())
                .contentType("application/json")
                .when().delete(VERSION+"/location/delete")
                .then().statusCode(204);

        Assertions.assertEquals(captor1.getValue(), id);
    }
}
