package com.seven.ije.controllers;

import com.seven.ije.config.security.UserAuthentication;
import com.seven.ije.models.entities.User;
import com.seven.ije.models.records.BookingRecord;
import com.seven.ije.models.records.TicketRecord;
import com.seven.ije.models.responses.Response;
import com.seven.ije.services.BookingService;
import com.seven.ije.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    @Autowired
    TicketService ticketService;
    @Autowired
    BookingService bookingService;
    @Autowired
    Authentication userAuthentication;
    
    @GetMapping("/search")
    public ResponseEntity<Response> getResource(@RequestParam(name = "booking_no") UUID bookingNo){
        User sender = (User) userAuthentication.getPrincipal();
        BookingRecord bookingRecord = (BookingRecord) bookingService.get(bookingNo);

        if (bookingRecord == null) {       //If resource was not found
           throw new ResourceAccessException("Booking " + bookingRecord.bookingNo() + " is not available");
        }
        else if (!sender.getEmail().equals(bookingRecord.passengerEmail())) {//If user does not own booking
            return ResponseEntity.ok(Response.builder()
                    .isError(false)
                    .message("You have no reservations with this booking number")
                    .status(HttpStatus.OK)
                    .timestamp(LocalDateTime.now())
                    .build());
        }else {
            //Search for ticket
            TicketRecord tr = (TicketRecord) ticketService.getByBookingNo(bookingNo);
            if (tr == null) {       //If resource was not found
               throw new ResourceAccessException("Ticket with booking number " + bookingNo + " is not available");
            } else {
                return ResponseEntity.ok(Response.builder()
                        .data(Set.of(tr))
                        .isError(false)
                        .status(HttpStatus.FOUND)
                        .timestamp(LocalDateTime.now())
                        .build());
            }
        }
    }

}
