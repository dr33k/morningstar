package com.seven.ije.integration;

import com.seven.ije.controllers.LocationController;
import com.seven.ije.models.entities.Location;
import com.seven.ije.models.entities.LocationId;
import com.seven.ije.models.enums.StateCode;
import com.seven.ije.models.requests.LocationCreateRequest;
import com.seven.ije.repositories.LocationRepository;
import com.seven.ije.services.LocationService;
import config.TestConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@WebMvcTest(controllers = LocationController.class)
@ContextConfiguration(classes = {TestConfiguration.class}, loader = AnnotationConfigContextLoader.class)
@Transactional
public class LocationControllerTest {

    @Autowired
    LocationRepository locationRepository;

    @Mock
    LocationService locationService;
    Location location;
    MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        location = new Location();
//        LocationId lId = new LocationId(StateCode.ABIA, "01");
//        location.setLocationId(lId);
//        location.setStateName(lId.getStateCode().getStateName());
//        location.setStationName("Station Name");
//        locationRepository.save(location);
    }

    @AfterEach
    void tearDown(){
        location = null;
    }

    @Test
    void testShouldCreateLocation() throws Exception{
        final String STATION_NAME = "My First Abia Station";

        LocationCreateRequest request = new LocationCreateRequest();
        request.setStateCode(StateCode.ABIA);
        request.setStationName(STATION_NAME);


        mockMvc
                .perform(post("/administrator/location",request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("stationName").value(STATION_NAME))
                .andDo(log());
    }
}
