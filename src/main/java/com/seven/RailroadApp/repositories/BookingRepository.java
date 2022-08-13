package com.seven.RailroadApp.repositories;

import com.seven.RailroadApp.models.entities.Booking;
import com.seven.RailroadApp.models.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    Optional<Booking> findByBookingNo(UUID bookingNo);
    Iterable<Booking> findAllByPassenger(Long passengerId);
}
