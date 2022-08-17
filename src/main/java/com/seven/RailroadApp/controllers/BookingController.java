package com.seven.RailroadApp.controllers;

import com.seven.RailroadApp.config.security.UserAuthentication;
import com.seven.RailroadApp.models.entities.User;
import com.seven.RailroadApp.models.records.BookingRecord;
import com.seven.RailroadApp.models.records.UserRecord;
import com.seven.RailroadApp.models.requests.BookingRequest;
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
public class BookingController implements Controller<BookingRequest, UUID> {
    @Autowired
    BookingService bookingService;
    @Autowired
    UserService userService;
    @Autowired
    UserAuthentication userAuthentication;

    @Override
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

    @Override
    public ResponseEntity<Response> getResource(@RequestParam(name = "id") UUID uuid) {
        User sender = (User) userAuthentication.getInstance().getPrincipal();
        BookingRecord bookingRecord = (BookingRecord) bookingService.get(uuid);

        if (bookingRecord == null) {       //If resource was not found
            return ResponseEntity.status(404).body(Response.builder()
                    .isError(true)
                    .message("Booking " + bookingRecord.bookingNo() + " is not available")
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

    @Override
    public ResponseEntity<Response> createResource(@Valid @RequestBody BookingRequest request) {
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

    @Override
    public ResponseEntity<Response> updateResource(@Valid @RequestBody BookingRequest request) {
        User sender = (User) userAuthentication.getInstance().getPrincipal();
        BookingRecord bookingRecord = BookingRecord.copy(request);
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

    @Override
    public ResponseEntity<Response> deleteResource(@RequestParam(name = "id") UUID uuid) {
        User sender = (User) userAuthentication.getInstance().getPrincipal();

        BookingRecord bookingRecord = (BookingRecord) bookingService.get(uuid);
        if (!sender.getEmail().equals(bookingRecord.passengerEmail())) {//If user does not own booking
            return ResponseEntity.status(404).body(Response.builder()
                    .isError(true)
                    .message("You have no reservations by this booking number")
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        }

        Boolean deleted = bookingService.delete(uuid);
        return (deleted) ?
                ResponseEntity.ok(Response.builder()
                        .message("Booking: " + uuid + " deleted successfully")
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
