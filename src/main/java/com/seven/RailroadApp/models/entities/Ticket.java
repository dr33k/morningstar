package com.seven.RailroadApp.models.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name="ticket")
@Table(name="ticket")
@Data
@Builder
public class Ticket {
    @Id
    @SequenceGenerator(name = "ticket_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(generator = "ticket_sequence",strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name="userId",referencedColumnName="id")
    private User userId;

    @OneToOne
    @JoinColumn(name="bookingNo",referencedColumnName="bookingNo")
    private Booking bookingNo;

    @Column(nullable = false)
    private LocalDate validUntil=bookingNo.getTravelDate();

    @Column(nullable = false)
    private Boolean isValid;
}
