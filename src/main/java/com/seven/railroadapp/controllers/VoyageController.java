package com.seven.railroadapp.controllers;

import com.seven.railroadapp.models.records.VoyageRecord;
import com.seven.railroadapp.models.requests.VoyageCreateRequest;
import com.seven.railroadapp.models.requests.VoyageUpdateRequest;
import com.seven.railroadapp.models.responses.Response;
import com.seven.railroadapp.services.VoyageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
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
           throw new ResourceAccessException("This Voyage is not available, please make sure the details are entered correctly");
        }else {
            return ResponseEntity.ok(Response.builder()
                    .data(Set.of(voyageRecord))
                    .isError(false)
                    .status(HttpStatus.FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @PostMapping("/create")
    public ModelAndView createResource(@Valid @RequestBody VoyageCreateRequest request) {
        VoyageRecord voyageRecord = VoyageRecord.copy(request);

        voyageRecord = (VoyageRecord) voyageService.create(voyageRecord);
        if (voyageRecord == null) {       //If resource was not saved
           throw new RuntimeException("Voyage Could not be created. The locations selected aren't available at this time");
        }
       else {
            return new ModelAndView("redirect:/voyageDetails","voyageRecord",voyageRecord);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateResource(@Valid @RequestBody VoyageUpdateRequest request) {
        VoyageRecord voyageRecord = VoyageRecord.copy(request);
        voyageRecord = (VoyageRecord) voyageService.update(voyageRecord);
        if (voyageRecord == null) {       //If resource was not found
            throw new ResourceAccessException("This Voyage is not available, please make sure the details are entered correctly");
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
