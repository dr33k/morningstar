package com.seven.RailroadApp.controllers;

import com.seven.RailroadApp.config.security.UserAuthentication;
import com.seven.RailroadApp.models.entities.User;
import com.seven.RailroadApp.models.enums.BookingStatus;
import com.seven.RailroadApp.models.records.BookingRecord;
import com.seven.RailroadApp.models.requests.BookingCreateRequest;
import com.seven.RailroadApp.models.requests.BookingUpdateRequest;
import com.seven.RailroadApp.models.responses.Response;
import com.seven.RailroadApp.services.BookingService;
import com.seven.RailroadApp.services.UserService;
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
@RequestMapping("/booking")
public class BookingController{
    @Autowired
    BookingService bookingService;
    @Autowired
    UserService userService;
    @Autowired
    UserAuthentication userAuthentication;

    @GetMapping
    public ResponseEntity<Response> getAllResources() {
        User sender = (User) userAuthentication.getInstance().getPrincipal();
        Set<BookingRecord> bookingRecords = bookingService.getAllByPassenger(sender.getEmail());
        return ResponseEntity.ok(Response.builder()
                .data(bookingRecords)
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<Response> getResource(@Valid @RequestParam(name = "id") UUID id) {
        User sender = (User) userAuthentication.getInstance().getPrincipal();
        BookingRecord bookingRecord = (BookingRecord) bookingService.get(id);

        if (bookingRecord == null) {       //If resource was not found
            return ResponseEntity.status(404).body(Response.builder()
                    .isError(true)
                    .message("Booking " + id + " is not available")
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        } else if (!(bookingRecord.message() == null)) {   //If there was an error during the process
            return ResponseEntity.status(404).body(Response.builder()
                    .isError(true)
                    .message(bookingRecord.message())
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
        else if (!sender.getEmail().equals(bookingRecord.passengerEmail())) {//If user does not own booking
            return ResponseEntity.ok(Response.builder()
                    .isError(false)
                    .message("You have no reservations by this booking number")
                    .status(HttpStatus.OK)
                    .timestamp(LocalDateTime.now())
                    .build());
        }else {
            return ResponseEntity.ok(Response.builder()
                    .data(Set.of(bookingRecord))
                    .isError(false)
                    .status(HttpStatus.FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createResource(@Valid @RequestBody BookingCreateRequest request) {
        BookingRecord bookingRecord = BookingRecord.copy(request);

        bookingRecord = (BookingRecord) bookingService.create(bookingRecord);
        if (bookingRecord == null) {       //If resource was not saved
            return ResponseEntity.badRequest().body(Response.builder()
                    .isError(true)
                    .message("Booking could not be created." +
                            "*The voyage selected isn't available at this time")
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        } else if (!(bookingRecord.message() == null)) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .isError(true)
                    .message(bookingRecord.message())
                    .status(HttpStatus.BAD_REQUEST)
                    .timestamp(LocalDateTime.now())
                    .build());
        } else {
            return ResponseEntity.created(URI.create("/dashboard")).body(Response.builder()
                    .data(Set.of(bookingRecord))
                    .isError(false)
                    .status(HttpStatus.CREATED)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateResource(@Valid @RequestParam(name = "id") UUID id, @Valid @RequestParam(name = "status")BookingStatus status) {
        User sender = (User) userAuthentication.getInstance().getPrincipal();
        BookingRecord bookingRecord = new BookingRecord(id,null,null,null,null,null,status,null);
        bookingRecord = (BookingRecord) bookingService.update(bookingRecord);

        if (bookingRecord == null) {       //If resource was not found
            return ResponseEntity.of(Optional.of(Response.builder()
                    .isError(true)
                    .message("A couple of things you might try checking:" +
                            "\n*Only the Status of the reservation can be modified." +
                            "\n*Make sure the id was entered correctly")
                    .status(HttpStatus.NOT_MODIFIED)
                    .timestamp(LocalDateTime.now())
                    .build()));
        } else if (!(bookingRecord.message() == null)) {   //If there was an error during the process
            return ResponseEntity.of(Optional.of(Response.builder()
                    .isError(true)
                    .message(bookingRecord.message())
                    .status(HttpStatus.NOT_MODIFIED)
                    .timestamp(LocalDateTime.now())
                    .build()));
        }else if (!sender.getEmail().equals(bookingRecord.passengerEmail())) {//If user does not own booking
            return ResponseEntity.status(404).body(Response.builder()
                    .isError(true)
                    .message("You have no reservations by this booking number")
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        } else {
            return ResponseEntity.ok(Response.builder()
                    .data(Set.of(bookingRecord))
                    .isError(false)
                    .status(HttpStatus.OK)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteResource(@Valid @RequestParam(name = "id") UUID id) {
        User sender = (User) userAuthentication.getInstance().getPrincipal();
        BookingRecord bookingRecord = (BookingRecord) bookingService.get(id);

        if (!sender.getEmail().equals(bookingRecord.passengerEmail())) {//If user does not own booking
            return ResponseEntity.status(404).body(Response.builder()
                    .isError(true)
                    .message("You have no reservations by this booking number")
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        }

        Boolean deleted = bookingService.delete(bookingRecord.bookingNo());
        return (deleted) ?
                ResponseEntity.ok(Response.builder()
                        .message("Booking: " + id + " deleted successfully")
                        .isError(false)
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now())
                        .build())
                :
                ResponseEntity.status(404).body(Response.builder()
                        .isError(true)
                        .message("Could not delete reservation." +
                                "Try checking your credentials." +
                                "Also, please cancel reservation before deleting")
                        .status(HttpStatus.NOT_FOUND)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}