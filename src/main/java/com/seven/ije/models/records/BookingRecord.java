package com.seven.ije.models.records;

import com.seven.ije.models.entities.Booking;
import com.seven.ije.models.enums.BookingStatus;
import com.seven.ije.models.enums.SeatType;
import com.seven.ije.models.requests.BookingCreateRequest;
import com.seven.ije.models.requests.BookingUpdateRequest;

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
