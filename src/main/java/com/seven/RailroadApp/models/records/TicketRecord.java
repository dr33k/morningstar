package com.seven.RailroadApp.models.records;

import com.seven.RailroadApp.models.entities.Booking;
import com.seven.RailroadApp.models.entities.Ticket;
import com.seven.RailroadApp.models.enums.BookingStatus;

import java.time.LocalDate;
import java.util.UUID;

public record TicketRecord(
        UUID bookingNo,
        LocalDate expiryDate,
        BookingStatus status
) {
    public static TicketRecord copy(Ticket t){
        return new TicketRecord(
                t.getBooking().getBookingNo(),
                t.getExpiryDate(),
                t.getStatus()
        );
    }
}