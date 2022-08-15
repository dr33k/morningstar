package com.seven.RailroadApp.models.records;

import com.seven.RailroadApp.models.entities.Booking;
import com.seven.RailroadApp.models.entities.Location;
import com.seven.RailroadApp.models.entities.Ticket;
import com.seven.RailroadApp.models.entities.User;
import com.seven.RailroadApp.models.enums.BookingStatus;
import com.seven.RailroadApp.models.enums.SeatType;
import com.seven.RailroadApp.models.enums.TravelTime;
import com.seven.RailroadApp.models.requests.BookingRequest;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record BookingRecord(
        UUID bookingNo,
        Location departureLocation,
        Location arrivalLocation,
        User passengerId,
        LocalDateTime bookingDate,
        LocalDate travelDate,
        TravelTime travelTime,
        SeatType seatType,
        BookingStatus status
) {
    public static BookingRecord copy(Booking b){
        return new BookingRecord(
                b.getBookingNo(),
                b.getDepartureLocation(),
                b.getArrivalLocation(),
                b.getPassenger(),
                b.getBookingDate(),
                b.getTravelDate(),
                b.getTravelTime(),
                b.getSeatType(),
                b.getStatus()
        );
    }
    public static BookingRecord copy(BookingRequest b){
        return new BookingRecord(
                null,
                b.getDepartureLocation(),
                b.getArrivalLocation(),
                null,
                null,
                b.getTravelDate(),
                b.getTravelTime(),
                b.getSeatType(),
                null
        );
    }
}