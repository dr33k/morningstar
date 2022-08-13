package com.seven.RailroadApp.models.entities;

import com.seven.RailroadApp.models.enums.BookingStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name="ticket")
@Table(name="ticket")
@Data
public class Ticket {
    @Id
    @SequenceGenerator(name = "ticket_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(generator = "ticket_sequence",strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @JoinColumn(name="bookingNo",referencedColumnName="bookingNo")
    private Booking booking;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
