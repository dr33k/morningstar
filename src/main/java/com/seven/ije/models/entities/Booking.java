package com.seven.ije.models.entities;

import com.seven.ije.models.enums.BookingStatus;
import com.seven.ije.models.enums.SeatType;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name="booking")
@Table(name="booking")
@Data
@ToString
public class Booking {
    @Id
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID bookingNo;

    @ManyToOne
    @Column(name = "voyageNo")
    private UUID voyageNo;

    @OneToOne
    @JoinColumn(name = "passenger", referencedColumnName = "id", table = "r_user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User passenger;

    @Column(nullable = false)
    private LocalDateTime bookingDateTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(nullable = false)
    private Boolean isPaid;
}
