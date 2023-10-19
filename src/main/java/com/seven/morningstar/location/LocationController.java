package com.seven.morningstar.location;

import com.seven.morningstar.enums.StateCode;
import com.seven.morningstar.responses.Response;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.seven.morningstar.util.AppConstants.VERSION;
import static com.seven.morningstar.util.Responder.created;
import static com.seven.morningstar.util.Responder.ok;

@RestController
@RequestMapping(VERSION+"/administrator/location")
public class LocationController {
    LocationService locationService;
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<Response> getAllResources() {
        return ok(locationService.getAll());
    }
    @GetMapping("/search")
    public ResponseEntity<Response> getResource( @Valid @RequestParam StateCode stateCode, @Valid @RequestParam String stationNo) {
        return ok(locationService.get(new LocationId(stateCode, stationNo)));
    }
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> createResource(@Valid @RequestBody LocationCreateRequest request) {
        return created(locationService.create(request), VERSION+"/administrator/location");
    }
    @PutMapping("/update")
    public ResponseEntity<Response> updateResource( @Valid @RequestParam StateCode stateCode, @Valid @RequestParam String stationNo, @Valid @RequestBody LocationUpdateRequest request) {
        request.setLocationId(new LocationId(stateCode, stationNo));
        return ok(locationService.update(request));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteResource(@Valid @RequestParam StateCode stateCode, @Valid @RequestParam String stationNo) {
        locationService.delete(new LocationId(stateCode, stationNo));
       return ok(null);
    }
}