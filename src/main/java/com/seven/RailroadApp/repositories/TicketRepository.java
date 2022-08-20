package com.seven.RailroadApp.repositories;

import com.seven.RailroadApp.models.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {
    Optional<Ticket> findByBookingBookingNo(UUID bookingNo);
    void deleteByBookingBookingNo(UUID bookingNo);
}
