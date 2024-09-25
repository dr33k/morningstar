package com.seven.morningstar.backend.ticket;

import com.seven.morningstar.backend.booking.Booking;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.ZonedDateTime;

@Entity
@Table(name="ticket")
@Data
@ToString
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="bookingNo")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Booking booking;

    @Column(nullable = false)
    @CreationTimestamp
    private ZonedDateTime creationDateTime;

    @Column(nullable = false)
    private ZonedDateTime expiryDateTime;
}
