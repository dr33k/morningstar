package com.seven.morningstar.backend.ticket;

import com.seven.morningstar.backend.responses.Response;
import com.seven.morningstar.backend.booking.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

import static com.seven.morningstar.backend.util.AppConstants.VERSION;
import static com.seven.morningstar.backend.util.Responder.ok;

@RestController
@RequestMapping(VERSION+"/ticket")
public class TicketController {
    TicketService ticketService;
    BookingService bookingService;
    Authentication userAuthentication;

    public TicketController(TicketService ticketService ,
                            BookingService bookingService ,
                            Authentication userAuthentication) {
        this.ticketService = ticketService;
        this.bookingService = bookingService;
        this.userAuthentication = userAuthentication;
    }
    @GetMapping("/search")
    public ResponseEntity<Response> getResource(@Valid @RequestParam(name = "booking_no") UUID bookingNo){
        TicketRecord ticketRecord = ticketService.get(bookingNo);
        return ok(Set.of(ticketRecord));
    }
}
