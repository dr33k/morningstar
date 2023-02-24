package com.seven.ije.booking;

import com.seven.ije.voyage.Voyage;
import com.seven.ije.enums.BookingStatus;
import com.seven.ije.enums.SeatType;

import java.time.ZonedDateTime;
import java.util.UUID;

public record BookingRecord(
        UUID bookingNo ,
        Voyage voyage ,
        ZonedDateTime bookingDateTime ,
        SeatType seatType ,
        BookingStatus status ,
        Boolean isPaid ,
        String message
) {
    public BookingRecord() {
        this(null , null , null , null , null , null , null);
    }
    public static BookingRecord copy(Booking b) {
        return new BookingRecord(
                b.getBookingNo() ,
                b.getVoyage() ,
                b.getBookingDateTime() ,
                b.getSeatType() ,
                b.getStatus() ,
                b.getIsPaid() ,
                null
        );
    }

    public static BookingRecord copy(BookingUpdateRequest b) {
        return new BookingRecord(
                b.getBookingNo() ,
                null ,
                null ,
                null ,
                b.getStatus() ,
                null ,
                null
        );
    }

    public String toString() {
        return this.bookingNo.toString();
    }
}