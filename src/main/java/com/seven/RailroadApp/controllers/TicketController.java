package com.seven.RailroadApp.controllers;

import com.seven.RailroadApp.models.records.TicketRecord;
import com.seven.RailroadApp.models.records.TicketRecord;
import com.seven.RailroadApp.models.responses.Response;
import com.seven.RailroadApp.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    @Autowired
    TicketService ticketService;
    
    @GetMapping("/search")
    public ResponseEntity<Response> getResource(@RequestParam(name = "booking_no") UUID bookingNo){
        TicketRecord tr = (TicketRecord) ticketService.getByBookingNo(bookingNo);
        if (tr == null) {       //If resource was not found
            return ResponseEntity.status(404).body(Response.builder()
                    .isError(true)
                    .message("Ticket with booking number " + bookingNo + " is not available")
                    .status(HttpStatus.NOT_FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        }else {
            return ResponseEntity.ok(Response.builder()
                    .data(Set.of(tr))
                    .isError(false)
                    .status(HttpStatus.FOUND)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }
    
    @GetMapping("/adminSearch")
    public ResponseEntity<Response> getAllResources() {
        Set<TicketRecord> ticketRecords = ticketService.getAll();
        return ResponseEntity.ok(Response.builder()
                .data(ticketRecords)
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
