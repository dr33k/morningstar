package com.seven.RailroadApp.controllers;

import com.seven.RailroadApp.models.entities.LocationId;
import com.seven.RailroadApp.models.records.LocationRecord;
import com.seven.RailroadApp.models.requests.LocationCreateRequest;
import com.seven.RailroadApp.models.requests.LocationUpdateRequest;
import com.seven.RailroadApp.models.responses.Response;
import com.seven.RailroadApp.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
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
            return ResponseEntity.status(404).body(Response.builder()
                    .isError(true)
                    .message("Location is not available")
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
        else if(!(locationRecord.message() == null)) {   //If there was an error during the process
            return ResponseEntity.status(404).body(Response.builder()
                    .isError(true)
                    .message(locationRecord.message())
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
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
    public ResponseEntity<Response> createResource(@Valid @RequestBody LocationCreateRequest request) {
        LocationRecord locationRecord = LocationRecord.copy(request);

        locationRecord = (LocationRecord) locationService.create(locationRecord);
        return (!(locationRecord.message() == null))?
                ResponseEntity.badRequest().body(Response.builder()
                        .isError(true)
                        .message(locationRecord.message())
                        .status(HttpStatus.BAD_REQUEST)
                        .timestamp(LocalDateTime.now())
                        .build())
                :
                ResponseEntity.created(URI.create("/dashboard")).body(Response.builder()
                        .data(Set.of(locationRecord))
                        .isError(false)
                        .status(HttpStatus.CREATED)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateResource(@Valid @RequestBody LocationUpdateRequest request) {
        LocationRecord locationRecord = LocationRecord.copy(request);
        locationRecord = (LocationRecord) locationService.update(locationRecord);
        if(locationRecord == null) {       //If resource was not found
            return ResponseEntity.of(Optional.of(Response.builder()
                    .isError(true)
                    .message("A couple of things you might try checking:" +
                            "\nYou cannot delete a used location, you can only deactivate" +
                            "\n*Only the Station Name and status can be modified. If you wish to modify something else, please deactivate this location and create another one" +
                            "\n*Make sure the id was entered correctly" +
                            "\n*Make sure you are not trying to update with the same information as before")
                    .status(HttpStatus.NOT_MODIFIED)
                    .timestamp(LocalDateTime.now())
                    .build()));
        }
        else if(!(locationRecord.message() == null)) {   //If there was an error during the process
            return ResponseEntity.of(Optional.of(Response.builder()
                    .isError(true)
                    .message(locationRecord.message())
                    .status(HttpStatus.NOT_MODIFIED)
                    .timestamp(LocalDateTime.now())
                    .build()));
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