package com.seven.ije.models.records;

import com.seven.ije.models.entities.Ticket;

import java.time.ZonedDateTime;
import java.util.UUID;

public record TicketRecord(
        UUID bookingNo,
        ZonedDateTime creationDate,
        ZonedDateTime expiryDate
) {
    public static TicketRecord copy(Ticket t){
        return new TicketRecord(
                t.getBooking().getBookingNo(),
                t.getCreationDateTime(),
                t.getExpiryDateTime()
        );
    }

    public String toString(){
        return this.bookingNo.toString();
    }
}