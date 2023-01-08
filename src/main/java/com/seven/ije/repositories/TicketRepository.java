package com.seven.ije.repositories;

import com.seven.ije.models.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {
    Optional<Ticket> findByBookingBookingNo(UUID bookingNo);
    Optional<Ticket> findByBookingPassengerIdAndBookingBookingNo(Long id, UUID BookingNo);
}
