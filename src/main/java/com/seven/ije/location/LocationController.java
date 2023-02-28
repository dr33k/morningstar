package com.seven.ije.location;

import com.seven.ije.responses.Response;
import com.seven.ije.util.Responder;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Set;

import static com.seven.ije.util.AppConstants.VERSION;
import static com.seven.ije.util.Responder.ok;
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
    public ResponseEntity<Response> getResource( @Valid @RequestParam LocationId locationId) {
        return ok(Set.of(locationService.get(locationId)));
    }
    @PostMapping("/create")
    public ResponseEntity<Response> createResource(@Valid @RequestBody LocationCreateRequest request) {
        return Responder.created(Set.of(locationService.create(request)),"/location");
    }
    @PutMapping("/update")
    public ResponseEntity<Response> updateResource(@Valid @RequestBody LocationUpdateRequest request) {
        return ok(Set.of(locationService.update(request)));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteResource(@Valid @RequestBody LocationId locationId) {
        locationService.delete(locationId);
       return ok(Collections.emptySet());
    }
}