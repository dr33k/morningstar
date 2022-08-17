package com.seven.RailroadApp.controllers;

import com.seven.RailroadApp.models.entities.User;
import com.seven.RailroadApp.models.records.BookingRecord;
import com.seven.RailroadApp.models.records.TicketRecord;
import com.seven.RailroadApp.models.records.UserRecord;
import com.seven.RailroadApp.models.requests.BookingRequest;
import com.seven.RailroadApp.models.requests.UserUpdateRequest;
import com.seven.RailroadApp.models.responses.Response;
import com.seven.RailroadApp.services.BookingService;
import com.seven.RailroadApp.services.TicketService;
import com.seven.RailroadApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController("/administrator")
public class AdminController {

    @Autowired
    TicketService ticketService;
    @Autowired
    UserService userService;

    @Autowired
    BookingService bookingService;

    @GetMapping("/tickets")
    public ResponseEntity<Response> getAllTickets() {
        Set<TicketRecord> ticketRecords = ticketService.getAll();
        return ResponseEntity.ok(Response.builder()
                .data(ticketRecords)
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<Response> getTicket(@RequestParam(name = "booking_no") UUID bookingNo) {
        //Search for ticket
        TicketRecord tr = (TicketRecord) ticketService.getByBookingNo(bookingNo);
        if (tr == null) {       //If resource was not found
            return ResponseEntity.status(404).body(Response.builder()
                    .isError(true)
                    .message("Ticket with booking number " + bookingNo + " is not available")
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        } else {
            return ResponseEntity.ok(Response.builder()
                    .data(Set.of(tr))
                    .isError(false)
                    .status(HttpStatus.FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        }

    }

    @GetMapping("/users")
    public ResponseEntity<Response> getAllUsers() {
        Set<UserRecord> userRecords = userService.getAll();
        return ResponseEntity.ok(Response.builder()
                .data(userRecords)
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/users/search")
    public ResponseEntity<Response> getUser(@RequestParam(name = "email") String email) {
        UserRecord userRecord = (UserRecord) userService.get(email);
        if (userRecord == null) {       //If resource was not found
            return ResponseEntity.status(404).body(Response.builder()
                    .isError(true)
                    .message("User is not available")
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        } else if (!(userRecord.message() == null)) {   //If there was an error during the process
            return ResponseEntity.status(404).body(Response.builder()
                    .isError(true)
                    .message(userRecord.message())
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        } else {
            return ResponseEntity.ok(Response.builder()
                    .data(Set.of(userRecord))
                    .isError(false)
                    .status(HttpStatus.FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @PutMapping("/update_user")
    public ResponseEntity<Response> updateUserRole(@Valid @RequestBody UserUpdateRequest request) {
        UserRecord userRecord = UserRecord.copy(request);
        userRecord = (UserRecord) userService.updateUserRoleForAdmin(userRecord);
        if (userRecord == null) {       //If resource was not found
            return ResponseEntity.of(Optional.of(Response.builder()
                    .isError(true)
                    .message("A couple of things you might try checking:" +
                            "\n*Make sure the id was entered correctly" +
                            "\n*Make sure you are not trying to update with the same information as before")
                    .status(HttpStatus.NOT_MODIFIED)
                    .timestamp(LocalDateTime.now())
                    .build()));
        } else if (!(userRecord.message() == null)) {   //If there was an error during the process
            return ResponseEntity.of(Optional.of(Response.builder()
                    .isError(true)
                    .message(userRecord.message())
                    .status(HttpStatus.NOT_MODIFIED)
                    .timestamp(LocalDateTime.now())
                    .build()));
        } else {
            return ResponseEntity.ok(Response.builder()
                    .data(Set.of(userRecord))
                    .isError(false)
                    .status(HttpStatus.OK)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @GetMapping("/bookings")
    public ResponseEntity<Response> getAllBookings() {
        Set<BookingRecord> bookingRecords = bookingService.getAll();
        return ResponseEntity.ok(Response.builder()
                .data(bookingRecords)
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/passenger_bookings")
    public ResponseEntity<Response> getAllBookingsByPassenger(@RequestParam(name = "passenger_id") String email) {
        UserRecord ur = (UserRecord) userService.get(email);
        if (ur == null) {       //If resource was not found
            return ResponseEntity.status(404).body(Response.builder()
                    .isError(true)
                    .message("User " + email + " is not available")
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        } else if (!(ur.message() == null)) {   //If there was an error during the process
            return ResponseEntity.status(404).body(Response.builder()
                    .isError(true)
                    .message(ur.message())
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        } else {
            Set<BookingRecord> bookingRecords = bookingService.getAllByPassenger(email);
            return ResponseEntity.ok(Response.builder()
                    .data(bookingRecords)
                    .isError(false)
                    .status(HttpStatus.OK)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @GetMapping("/bookings/search")
    public ResponseEntity<Response> getBooking(@RequestParam(name = "id") UUID uuid) {
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
        } else {
            return ResponseEntity.ok(Response.builder()
                    .data(Set.of(bookingRecord))
                    .isError(false)
                    .status(HttpStatus.FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @PutMapping("/update_user_booking")
    public ResponseEntity<Response> updateResource(@Valid @RequestBody BookingRequest request) {
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
        } else {
            return ResponseEntity.ok(Response.builder()
                    .data(Set.of(bookingRecord))
                    .isError(false)
                    .status(HttpStatus.OK)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }


}
