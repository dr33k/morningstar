package com.seven.RailroadApp.controllers;

import com.seven.RailroadApp.models.records.VoyageRecord;
import com.seven.RailroadApp.models.requests.VoyageCreateRequest;
import com.seven.RailroadApp.models.requests.VoyageUpdateRequest;
import com.seven.RailroadApp.models.responses.Response;
import com.seven.RailroadApp.services.VoyageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/administrator/voyage")
public class VoyageController {
    @Autowired
    VoyageService voyageService;

    @GetMapping
    public ResponseEntity<Response> getAllResources() {
        Set<VoyageRecord> voyageRecords = voyageService.getAll();
        return ResponseEntity.ok(Response.builder()
                .data(voyageRecords)
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<Response> getResource(@Valid @RequestParam(name = "id") UUID voyageNo) {
        VoyageRecord voyageRecord = (VoyageRecord) voyageService.get(voyageNo);
        if (voyageRecord == null) {       //If resource was not found
            return ResponseEntity.status(404).body(Response.builder()
                    .isError(true)
                    .message("Voyage is not available")
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        } else if (!(voyageRecord.message() == null)) {   //If there was an error during the process
            return ResponseEntity.status(404).body(Response.builder()
                    .isError(true)
                    .message(voyageRecord.message())
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        } else {
            return ResponseEntity.ok(Response.builder()
                    .data(Set.of(voyageRecord))
                    .isError(false)
                    .status(HttpStatus.FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createResource(@Valid @RequestBody VoyageCreateRequest request) {
        VoyageRecord voyageRecord = VoyageRecord.copy(request);

        voyageRecord = (VoyageRecord) voyageService.create(voyageRecord);
        if (voyageRecord == null) {       //If resource was not saved
            return ResponseEntity.badRequest().body(Response.builder()
                    .isError(true)
                    .message("Voyage Could not be created." +
                            "*The locations selected aren't available at this time")
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        } else if(!(voyageRecord.message() == null)) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .isError(true)
                    .message(voyageRecord.message())
                    .status(HttpStatus.BAD_REQUEST)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
        else {
            return ResponseEntity.created(URI.create("/dashboard")).body(Response.builder()
                    .data(Set.of(voyageRecord))
                    .isError(false)
                    .status(HttpStatus.CREATED)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateResource(@Valid @RequestBody VoyageUpdateRequest request) {
        VoyageRecord voyageRecord = VoyageRecord.copy(request);
        voyageRecord = (VoyageRecord) voyageService.update(voyageRecord);
        if (voyageRecord == null) {       //If resource was not found
            return ResponseEntity.of(Optional.of(Response.builder()
                    .isError(true)
                    .message("A couple of things you might try checking:" +
                            "\n*Only the Status of the voyage can be modified." +
                            "\n*Make sure the id was entered correctly")
                    .status(HttpStatus.NOT_MODIFIED)
                    .timestamp(LocalDateTime.now())
                    .build()));
        } else if (!(voyageRecord.message() == null)) {   //If there was an error during the process
            return ResponseEntity.of(Optional.of(Response.builder()
                    .isError(true)
                    .message(voyageRecord.message())
                    .status(HttpStatus.NOT_MODIFIED)
                    .timestamp(LocalDateTime.now())
                    .build()));
        } else {
            return ResponseEntity.ok(Response.builder()
                    .data(Set.of(voyageRecord))
                    .isError(false)
                    .status(HttpStatus.OK)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

}
