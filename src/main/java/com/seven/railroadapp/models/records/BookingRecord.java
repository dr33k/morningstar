package com.seven.railroadapp.models.records;

import com.seven.railroadapp.models.entities.Booking;
import com.seven.railroadapp.models.enums.BookingStatus;
import com.seven.railroadapp.models.enums.SeatType;
import com.seven.railroadapp.models.requests.BookingCreateRequest;
import com.seven.railroadapp.models.requests.BookingUpdateRequest;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingRecord(
        UUID bookingNo,
        UUID voyageNo,
        String passengerEmail,
        LocalDateTime bookingDate,
        SeatType seatType,
        BookingStatus status,
        Boolean isPaid,
        String message
) {
    public static BookingRecord copy(Booking b){
        return new BookingRecord(
                b.getBookingNo(),
                b.getVoyage().getVoyageNo(),
                b.getPassenger().getEmail(),
                b.getBookingDateTime(),
                b.getSeatType(),
                b.getStatus(),
                b.getIsPaid(),
                null
        );
    }
     public static BookingRecord copy(BookingCreateRequest b){
        return new BookingRecord(
                null,
                b.getVoyageNo(),
                null,
                null,
                b.getSeatType(),
                null,
                null,
                null
        );
    }
    public static BookingRecord copy(BookingUpdateRequest b){
        return new BookingRecord(
                b.getBookingNo(),
                null,
                null,
                null,
                null,
                b.getStatus(),
                null,
                null
        );
    }
    public String toString(){
        return this.bookingNo.toString();
    }

}
