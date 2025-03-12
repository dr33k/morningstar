package com.seven.morningstar.location;

import com.seven.morningstar.enums.StateCode;
import com.seven.morningstar.responses.Response;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.seven.morningstar.util.AppConstants.VERSION;
import static com.seven.morningstar.util.Responder.*;

@RestController
@RequestMapping(value = VERSION+"/location", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "jwtAuth")

public class LocationController {
    LocationService locationService;
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('location:r')")
    public ResponseEntity<Response> getAllResources() {
        return ok(locationService.getAll());
    }

    @GetMapping(value = "/search")
    @PreAuthorize("hasAuthority('location:r')")
    public ResponseEntity<Response> getResource( @Valid @RequestParam StateCode stateCode, @Valid @RequestParam String stationNo) {
        return ok(locationService.get(new LocationId(stateCode, stationNo)));
    }
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> createResource(@Valid @RequestBody LocationCreateRequest request) {
        return created(locationService.create(request), VERSION+"/administrator/location");
    }
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> updateResource( @Valid @RequestParam StateCode stateCode, @Valid @RequestParam String stationNo, @Valid @RequestBody LocationUpdateRequest request) {
        request.setLocationId(new LocationId(stateCode, stationNo));
        return ok(locationService.update(request));
    }
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteResource(@Valid @RequestParam StateCode stateCode, @Valid @RequestParam String stationNo) {
        locationService.delete(new LocationId(stateCode, stationNo));
        return noContent();
    }
}