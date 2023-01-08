package com.seven.ije.models.entities;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
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
