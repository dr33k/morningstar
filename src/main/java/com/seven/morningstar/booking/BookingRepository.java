package com.seven.morningstar.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("bookingRepository")
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findAllByPassengerId(Long id);
    List<Booking> findAllByPassengerEmail(String email);
    Optional <Booking> findByPassengerIdAndBookingNo(Long id, UUID bookingNo);
    Integer deleteByPassengerIdAndBookingNoAndStatusNot(Long id, UUID bookingNo, String status);

    //For Admin
    Integer deleteByBookingNoAndStatusNot(UUID bookingNo, String status);
}
