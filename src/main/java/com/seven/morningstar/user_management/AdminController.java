package com.seven.morningstar.user_management;

import com.seven.morningstar.config.security.Authorize;
import com.seven.morningstar.ticket.TicketRecord;
import com.seven.morningstar.booking.BookingUpdateRequest;
import com.seven.morningstar.responses.Response;
import com.seven.morningstar.booking.BookingService;
import com.seven.morningstar.ticket.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import static com.seven.morningstar.util.Responder.ok;
import static com.seven.morningstar.util.AppConstants.VERSION;
@RestController
@RequestMapping(VERSION+"/administrator")
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
        return ok(Set.of(ticketService.get(bookingNo)));
    }

    @Authorize(roles = "ADMIN")
    @GetMapping("/passengers")
    public ResponseEntity <Response> getAllUsers() {
        return ok(userService.getAll());
    }

    @GetMapping("/passengers/search")
    public ResponseEntity <Response> getUser(@Valid @RequestParam(name = "email") String email) {
        return ok(Set.of(userService.get(email)));//Signifies admin access
    }

    @PatchMapping("/passengers/update")
    public ResponseEntity <Response> updateUserRole(@Valid @RequestBody UserUpdateRequest request) {
        return ok(Set.of(userService.updateForAdmin(request)));
    }

    @GetMapping("/bookings")
    public ResponseEntity <Response> getAllBookings() {
        return ok(bookingService.getAll());
    }
    @GetMapping("/bookings/passengers")
    public ResponseEntity <Response> getAllBookingsByPassenger(@RequestParam(name = "email") String email) {
        return ok(bookingService.getAllByPassenger(email));
    }

    @GetMapping("/bookings/search")
    public ResponseEntity <Response> getBooking(@Valid @RequestParam(name = "id") UUID id) {
        return ok(Set.of(bookingService.get(id)));
    }

    @PatchMapping("/bookings/passengers/update")
    public ResponseEntity <Response> updateUserBookingStatus(@Valid @RequestBody BookingUpdateRequest request) {
        return ok(Set.of(bookingService.update(request)));
    }
}