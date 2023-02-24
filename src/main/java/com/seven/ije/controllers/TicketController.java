package com.seven.ije.controllers;

import com.seven.ije.models.records.TicketRecord;
import com.seven.ije.models.responses.Response;
import com.seven.ije.services.BookingService;
import com.seven.ije.services.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

import static com.seven.ije.util.Responder.ok;

@RestController
@RequestMapping("/ticket")
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
