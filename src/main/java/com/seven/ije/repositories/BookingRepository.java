package com.seven.ije.repositories;

import com.seven.ije.models.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findAllByPassenger(Long id);
    Optional <Booking> findByPassengerAndBookingNo(Long id, UUID bookingNo);
    Integer deleteByPassengerAndBookingNoAndStatusNot(Long id, UUID bookingNo, String status);

    //For Admin
    Integer deleteByBookingNoAndStatusNot(UUID bookingNo, String status);
}
