package com.seven.ije.controllers;

import com.seven.ije.models.records.BookingRecord;
import com.seven.ije.models.records.TicketRecord;
import com.seven.ije.models.records.UserRecord;
import com.seven.ije.models.requests.BookingUpdateRequest;
import com.seven.ije.models.requests.UserUpdateRequest;
import com.seven.ije.models.responses.Response;
import com.seven.ije.services.BookingService;
import com.seven.ije.services.TicketService;
import com.seven.ije.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;
import static com.seven.ije.util.Responder.ok;

@RestController
@RequestMapping("/administrator")
public class AdminController {
    TicketService ticketService;
    UserService userService;
    BookingService bookingService;

    public AdminController(TicketService ticketService , UserService userService , BookingService bookingService) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @GetMapping("/tickets")
    public ResponseEntity <Response> getAllTickets() {
        Set <TicketRecord> ticketRecords = ticketService.getAll();
        return ok(ticketRecords);
    }

    @GetMapping("/tickets/search")
    public ResponseEntity <Response> getTicket(@RequestParam(name = "booking_no") UUID bookingNo) {
        TicketRecord ticketRecord = (TicketRecord) ticketService.get(bookingNo);
        return ok(Set.of(ticketRecord));
    }

    @GetMapping("/passengers")
    public ResponseEntity <Response> getAllUsers() {
        Set <UserRecord> userRecords = userService.getAll();
        return ok(userRecords);
    }

    @GetMapping("/passengers/search")
    public ResponseEntity <Response> getUser(@Valid @RequestParam(name = "email") String email) {
        UserRecord userRecord = (UserRecord) userService.get(email); //Signifies admin access
        return ok(Set.of(userRecord));
    }

    @PatchMapping("/passengers/update")
    public ResponseEntity <Response> updateUserRole(@Valid @RequestBody UserUpdateRequest request) {
        UserRecord userRecord = (UserRecord) userService.updateForAdmin(request);
        return ok(Set.of(userRecord));
    }

    @GetMapping("/bookings")
    public ResponseEntity <Response> getAllBookings() {
        Set <BookingRecord> bookingRecords = bookingService.getAll();
        return ok(bookingRecords);
    }
    @GetMapping("/bookings/passengers")
    public ResponseEntity <Response> getAllBookingsByPassenger(@RequestParam(name = "email") String email) {
        Set <BookingRecord> bookingRecords = bookingService.getAllByPassenger(email);
        return ok(bookingRecords);
    }

    @GetMapping("/bookings/search")
    public ResponseEntity <Response> getBooking(@Valid @RequestParam(name = "id") UUID id) {
        BookingRecord bookingRecord = (BookingRecord) bookingService.get(id);
        return ok(Set.of(bookingRecord));
    }

    @PatchMapping("/bookings/passengers/update")
    public ResponseEntity <Response> updateUserBookingStatus(@Valid @RequestBody BookingUpdateRequest request) {
        BookingRecord bookingRecord = bookingService.update(request);
        return ok(Set.of(bookingRecord));
    }
}