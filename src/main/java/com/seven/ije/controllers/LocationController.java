package com.seven.ije.controllers;

import com.seven.ije.models.entities.LocationId;
import com.seven.ije.models.requests.LocationCreateRequest;
import com.seven.ije.models.requests.LocationUpdateRequest;
import com.seven.ije.models.responses.Response;
import com.seven.ije.services.LocationService;
import com.seven.ije.util.Responder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.Set;
import static com.seven.ije.util.Responder.ok;
@RestController
@RequestMapping("/administrator/location")
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