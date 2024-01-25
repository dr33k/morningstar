package com.seven.morningstar.unit.controllers;

import com.seven.morningstar.enums.LocationStatus;
import com.seven.morningstar.enums.StateCode;
import com.seven.morningstar.location.LocationController;
import com.seven.morningstar.location.LocationId;
import com.seven.morningstar.location.LocationRecord;
import com.seven.morningstar.location.LocationService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static com.seven.morningstar.util.AppConstants.VERSION;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class LocationControllerTest {
    @Mock
    LocationService locationService;
    @InjectMocks
    LocationController locationController;

    @BeforeEach
    void init(){
        standaloneSetup(locationController);
    }

    @Test
    void testShouldGetAllLocations(){
        Mockito.when(locationService.getAll()).thenReturn(Collections.emptySet());
        given().contentType("application/json")
                .when().get(VERSION+"/administrator/location").then().statusCode(200);
    }

    @Test
    void testShouldGetLocation(){
        LocationId id = new LocationId(StateCode.ABUJ, "01");
        LocationRecord result = testlocationRecord(id);
        Mockito.when(locationService.get(Mockito.any(LocationId.class))).thenReturn(result);

        given()
                .param("stateCode", id.getStateCode())
                .param("stationNo", id.getStationNo())
                .contentType("application/json")
                .when()
                .get(VERSION+"/administrator/location/search")
                .then()
                .statusCode(200)
                .body("data.locationId.stateCode", Matchers.equalTo( id.getStateCode().name()));
    }

    private LocationRecord testlocationRecord(LocationId id){
        return new LocationRecord(id, "Abuja Station", "Abuja", LocationStatus.UNUSED, null);
    }

}
