package com.seven.RailroadApp.models.entities;

import com.seven.RailroadApp.models.enums.BookingStatus;
import com.seven.RailroadApp.models.enums.SeatType;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name="booking")
@Table(name="booking")
@Data
public class Booking {
    @Id
    private UUID bookingNo;

    @ManyToOne
    @JoinColumn(name = "voyageNo", referencedColumnName = "voyageNo")
    private Voyage voyage;

    @OneToOne
    @JoinColumn(name = "passengerId", referencedColumnName = "id")
    private User passenger;

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
