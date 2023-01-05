package com.seven.ije.models.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

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
    @JoinColumn(name="bookingNo",referencedColumnName="bookingNo",table = "booking")
    private UUID bookingNo;

    @Column(nullable = false)
    private LocalDateTime expiryDateTime;
}
