package com.seven.ije.integration;

import com.seven.ije.location.LocationController;
import config.TestConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

@WebMvcTest(controllers = LocationController.class)
@ContextConfiguration(classes = {TestConfiguration.class}, loader = AnnotationConfigContextLoader.class)
@Transactional
public class LocationControllerTest {
//
//    @Autowired
//    LocationRepository locationRepository;
//
//    @Mock
//    LocationService locationService;
//    Location location;
//    MockMvc mockMvc;
//
//    @BeforeEach
//    void setUp(){
//        location = new Location();
////        LocationId lId = new LocationId(StateCode.ABIA, "01");
////        location.setLocationId(lId);
////        location.setStateName(lId.getStateCode().getStateName());
////        location.setStationName("Station Name");
////        locationRepository.save(location);
//    }
//
//    @AfterEach
//    void tearDown(){
//        location = null;
//    }
//
//    @Disabled
//    @Test
//    void testShouldCreateLocation() throws Exception{
//        final String STATION_NAME = "My First Abia Station";
//
//        LocationCreateRequest request = new LocationCreateRequest();
//        request.setStateCode(StateCode.ABIA);
//        request.setStationName(STATION_NAME);
//
//
//        mockMvc
//                .perform(post("/administrator/location",request))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("stationName").value(STATION_NAME))
//                .andDo(log());
//    }
}
