//package com.seven.ije.integration;
//
//import com.seven.ije.enums.StateCode;
//import com.seven.ije.location.*;
//import config.TestConfiguration;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//import org.springframework.transaction.annotation.Transactional;
//import static io.restassured.RestAssured.*;
//
//@WebMvcTest(controllers = LocationController.class)
//@ContextConfiguration(classes = {TestConfiguration.class}, loader = AnnotationConfigContextLoader.class)
//@Transactional
//@RequiredArgsConstructor
//public class LocationControllerTest {
//
//    LocationRepository locationRepository;
//
//    @Mock
//    LocationService locationService;
//
//    Location location;
//
//    @BeforeEach
//    void setUp(){
//        location = new Location();
//        LocationId lId = new LocationId(StateCode.ABIA, "01");
//        location.setLocationId(lId);
//        location.setStateName(lId.getStateCode().getStateName());
//        location.setStationName("Station Name");
//        locationRepository.save(location);
//    }
//
//    @AfterEach
//    void tearDown(){
//        location = null;
//        locationRepository.deleteAll();
//    }
//
//    @Test
//    @Disabled
//    void testShouldCreateLocation() throws Exception{
//        final String STATION_NAME = "My First Abia Station";
//
//        LocationCreateRequest request = new LocationCreateRequest();
//        request.setStateCode(StateCode.ABIA);
//        request.setStationName(STATION_NAME);
//    }
//}
