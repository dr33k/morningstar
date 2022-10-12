package com.seven.railroadapp.models.records;

import com.seven.railroadapp.models.entities.Ticket;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketRecord(
        UUID bookingNo,
        LocalDateTime expiryDate
) {
    public static TicketRecord copy(Ticket t){
        return new TicketRecord(
                t.getBooking().getBookingNo(),
                t.getExpiryDateTime()
        );
    }

    public String toString(){
        return this.bookingNo.toString();
    }
}