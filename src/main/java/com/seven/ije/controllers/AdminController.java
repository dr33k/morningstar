package com.seven.ije.controllers;

import com.seven.ije.models.enums.BookingStatus;
import com.seven.ije.models.enums.UserRole;
import com.seven.ije.models.records.BookingRecord;
import com.seven.ije.models.records.TicketRecord;
import com.seven.ije.models.records.UserRecord;
import com.seven.ije.models.requests.BookingUpdateRequest;
import com.seven.ije.models.responses.Response;
import com.seven.ije.services.BookingService;
import com.seven.ije.services.TicketService;
import com.seven.ije.services.UserService;
import io.swagger.annotations.SecurityDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.web.servlet.SecurityMarker;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/administrator")
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
    public ResponseEntity<Response> getUser(@Valid @RequestParam(name = "id")String id) {
        UserRecord userRecord = (UserRecord) userService.get(id);

        if (userRecord == null) {       //If resource was not found
           throw new ResourceAccessException("User "+id +" is not available. Make sure the user id was entered correctly");
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
    public ResponseEntity<Response> updateUserRole(@Valid @RequestParam(name = "id")String id,@Valid @RequestParam(name = "role") UserRole role) {
        UserRecord userRecord = new UserRecord(null,null,null,id,null,null,null,role,null,null,null,null,null);
        userRecord = (UserRecord) userService.updateUserRoleForAdmin(userRecord);
        if (userRecord == null) {       //If resource was not found
           throw new ResourceAccessException("User not found. Make sure the user id was entered correctly");
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
    public ResponseEntity<Response> getAllBookingsByPassenger(@RequestParam(name = "id") String id) {
        UserRecord ur = (UserRecord) userService.get(id);
        if (ur == null) {       //If resource was not found
            throw new ResourceAccessException("User " + id + " is not available");
        }else {
            Set<BookingRecord> bookingRecords = bookingService.getAllByPassenger();
            return ResponseEntity.ok(Response.builder()
                    .data(bookingRecords)
                    .isError(false)
                    .status(HttpStatus.OK)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @GetMapping("/bookings/search")
    public ResponseEntity<Response> getBooking(@Valid @RequestParam(name = "id")String id) {
        BookingRecord bookingRecord = (BookingRecord) bookingService.get(id);

        if (bookingRecord == null) {       //If resource was not found
            throw new ResourceAccessException("Booking " + id + " is not available");
        } else {
            return ResponseEntity.ok(Response.builder()
                    .data(Set.of(bookingRecord))
                    .isError(false)
                    .status(HttpStatus.FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @PatchMapping("/bookings/update")
    public ResponseEntity <Response> updateUserBookingStatus(@Valid @RequestBody BookingUpdateRequest request) {
        BookingRecord bookingRecord = bookingService.update(request);
        return ResponseEntity.ok(Response.builder()
                .data(Set.of(bookingRecord))
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build());
    }

}
