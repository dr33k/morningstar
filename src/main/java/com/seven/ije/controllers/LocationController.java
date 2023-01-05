package com.seven.ije.controllers;

import com.seven.ije.models.entities.LocationId;
import com.seven.ije.models.records.LocationRecord;
import com.seven.ije.models.requests.LocationCreateRequest;
import com.seven.ije.models.requests.LocationUpdateRequest;
import com.seven.ije.models.responses.Response;
import com.seven.ije.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Set;


@RestController
@RequestMapping("/administrator/location")
public class LocationController {
    @Autowired
    LocationService locationService;

    @GetMapping
    public ResponseEntity<Response> getAllResources() {
        Set<LocationRecord> locationRecords = locationService.getAll();
        return ResponseEntity.ok(Response.builder()
                .data(locationRecords)
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build());
    }
    @GetMapping("/search")
    public ResponseEntity<Response> getResource(@RequestParam(name = "stateCode") String stateCode,@RequestParam(name = "stationNo") String stationNo) {
        LocationId lId = new LocationId(stateCode,stationNo);
        LocationRecord locationRecord = (LocationRecord) locationService.get(lId);
        if(locationRecord == null) {       //If resource was not found
            throw new ResourceAccessException("Location is not available");
        }
        else {
            return ResponseEntity.ok(Response.builder()
                    .data(Set.of(locationRecord))
                    .isError(false)
                    .status(HttpStatus.FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @PostMapping("/create")
    public ModelAndView createResource(@Valid @RequestBody LocationCreateRequest request) {
        LocationRecord locationRecord = LocationRecord.copy(request);

        locationRecord = (LocationRecord) locationService.create(locationRecord);
        return new ModelAndView("redirect:/locationDetails","locationRecord",locationRecord);
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateResource(@Valid @RequestBody LocationUpdateRequest request) {
        LocationRecord locationRecord = LocationRecord.copy(request);
        locationRecord = (LocationRecord) locationService.update(locationRecord);
        if(locationRecord == null) {       //If resource was not found
            throw new ResourceAccessException("This location is unavailable");
        }
            else {
            return ResponseEntity.ok(Response.builder()
                    .data(Set.of(locationRecord))
                    .isError(false)
                    .status(HttpStatus.OK)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteResource(@RequestParam(name = "stateCode") String stateCode,@RequestParam(name = "stationNo") String stationNo) {
        LocationId lId = new LocationId(stateCode,stationNo);
        Boolean deleted = locationService.delete(lId);
        return (deleted)?
                ResponseEntity.ok(Response.builder()
                        .message("Location "+lId.getStateCode()+lId.getStationNo()+" deleted successfully")
                        .isError(false)
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now())
                        .build())
                :
                ResponseEntity.status(404).body(Response.builder()
                        .message("This location could not be deleted for the following reasons:" +
                                "\nIt is not available or not offered by this service" +
                                "\nIt has already been used as an arrival or destination location for journeys in the past" +
                                "\nIf you wish to deactivate this location, do so by updating its status")
                        .isError(false)
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}