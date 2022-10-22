package com.seven.ije.models.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="ticket")
@Table(name="ticket")
@Data
@ToString
public class Ticket {
    @Id
    @SequenceGenerator(name = "ticket_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(generator = "ticket_sequence",strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @JoinColumn(name="bookingNo",referencedColumnName="bookingNo")
    private Booking booking;

    @Column(nullable = false)
    private LocalDateTime expiryDateTime;
}
