package com.seven.RailroadApp.controllers;

import com.seven.RailroadApp.models.entities.LocationId;
import com.seven.RailroadApp.models.records.LocationRecord;
import com.seven.RailroadApp.models.requests.LocationRequest;
import com.seven.RailroadApp.models.responses.Response;
import com.seven.RailroadApp.services.LocationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("/administrator/location")
public class LocationController implements Controller<LocationRequest, LocationId> {
    @Autowired
    LocationService locationService;

    @Override
    public ResponseEntity<Response> getAllResources() {
        Set<LocationRecord> locationRecords = locationService.getAll();
        return ResponseEntity.ok(Response.builder()
                .data(locationRecords)
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @Override
    public ResponseEntity<Response> getResource(@Valid @RequestBody LocationId id) {
        LocationRecord locationRecord = (LocationRecord) locationService.get(id);
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

    @Override
    public ResponseEntity<Response> createResource(@Valid @RequestBody LocationRequest request) {
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

    @Override
    public ResponseEntity<Response> updateResource(@Valid @RequestBody LocationRequest request) {
        LocationRecord locationRecord = LocationRecord.copy(request);
        locationRecord = (LocationRecord) locationService.update(locationRecord);
        if(locationRecord == null) {       //If resource was not found
            return ResponseEntity.of(Optional.of(Response.builder()
                    .isError(true)
                    .message("A couple of things you might try checking:" +
                            "\n*Only the Station Name can be modified. If you wish to modify something else, please delete this location and create another one" +
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

    @Override
    public ResponseEntity<Response> deleteResource(@Valid @RequestBody LocationId id) {
        Boolean deleted = locationService.delete(id);
        return (deleted)?
                ResponseEntity.ok(Response.builder()
                        .message("Location "+id.getStateCode()+id.getStationNo()+" deleted successfully")
                        .isError(false)
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now())
                        .build())
                :
                ResponseEntity.notFound().build();
    }
}
