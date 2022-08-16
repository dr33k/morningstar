package com.seven.RailroadApp.models.records;

import com.seven.RailroadApp.models.entities.Booking;
import com.seven.RailroadApp.models.enums.BookingStatus;
import com.seven.RailroadApp.models.enums.SeatType;
import com.seven.RailroadApp.models.requests.BookingRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record BookingRecord(
        UUID bookingNo,
        UUID voyageNo,
        String passengerEmail,
        LocalDateTime bookingDate,
        LocalDate expiryDate,
        SeatType seatType,
        BookingStatus status,
        String message
) {
    public static BookingRecord copy(Booking b){
        return new BookingRecord(
                b.getBookingNo(),
                b.getVoyage().getVoyageNo(),
                b.getPassenger().getEmail(),
                b.getBookingDate(),
                b.getExpiryDate(),
                b.getSeatType(),
                b.getStatus(),
                null
        );
    }
    public static BookingRecord copy(BookingRequest b){
        return new BookingRecord(
                b.getBookingNo(),
                b.getVoyageNo(),
                null,
                null,
                null,
                b.getSeatType(),
                b.getStatus(),
                null
        );
    }
}
