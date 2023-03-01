package com.seven.ije.location;

import com.seven.ije.enums.StateCode;
import com.seven.ije.responses.Response;
import com.seven.ije.util.Responder;
import com.seven.ije.voyage.VoyageRecord;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.seven.ije.util.AppConstants.VERSION;
import static com.seven.ije.util.Responder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(VERSION+"/administrator/location")
public class LocationController {
    LocationService locationService;
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping(produces = {"application/xml","application/json"})
    public ResponseEntity<Response> getAllResources() {
        return ok(locationService.getAll());
    }
    @GetMapping("/search")
    public ResponseEntity<Response> getResource( @Valid @RequestParam StateCode stateCode, @Valid @RequestParam String stationNo) {
        return ok(Set.of(locationService.get(new LocationId(stateCode, stationNo))));
    }
    @PostMapping("/create")
    public EntityModel <ResponseEntity<Response>> createResource(@Valid @RequestBody LocationCreateRequest request) {
        Set<LocationRecord> records = Set.of(locationService.create(request));
        EntityModel<ResponseEntity<Response>> entityModel = createdHal(records,"/location");
        Map <String, Object> refToInvocationObjectMap = Map.of("view_all",methodOn(this.getClass()).getAllResources());
        createAndIncludeLinks(refToInvocationObjectMap, entityModel);
        return entityModel;
    }
    @PutMapping("/update")
    public ResponseEntity<Response> updateResource( @Valid @RequestParam StateCode stateCode, @Valid @RequestParam String stationNo, @Valid @RequestBody LocationUpdateRequest request) {
        request.setLocationId(new LocationId(stateCode, stationNo));
        return ok(Set.of(locationService.update(request)));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteResource(@Valid @RequestParam StateCode stateCode, @Valid @RequestParam String stationNo) {
        locationService.delete(new LocationId(stateCode, stationNo));
       return ok(Collections.emptySet());
    }
}