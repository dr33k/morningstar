package com.seven.morningstar.backend.booking;

import com.seven.morningstar.backend.location.enums.BookingStatus;
import com.seven.morningstar.backend.location.enums.SeatType;
import com.seven.morningstar.backend.user_management.User;
import com.seven.morningstar.backend.voyage.Voyage;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.BeanUtils;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name="booking")
@Data
@ToString
public class Booking {
    @Id
    @GeneratedValue
    private UUID bookingNo;

    @ManyToOne
    @JoinColumn(name="voyage_no")
    private Voyage voyage;

    @ManyToOne
    @JoinColumn(name = "passenger")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User passenger;

    @CreationTimestamp
    @Column(nullable = false)
    private ZonedDateTime bookingDateTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(nullable = false)
    private Boolean isPaid;

    public static Booking of(BookingRecord record) {
        Booking b = new Booking();
        BeanUtils.copyProperties(record, b);
        return b;
    }
    public static Booking of(BookingCreateRequest request) {
        Booking b = new Booking();
        BeanUtils.copyProperties(request, b);
        return b;
    }
}