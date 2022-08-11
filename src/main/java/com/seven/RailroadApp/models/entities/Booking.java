package com.seven.RailroadApp.models.entities;

import com.seven.RailroadApp.models.enums.SeatType;
import com.seven.RailroadApp.models.enums.TravelTime;
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
    @Column(columnDefinition ="booking_no uuid NOT NULL DEFAULT uuid_generate_v4()")
    private UUID bookingNo;

    //Join columns on composite primary key
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="departureStateCode",referencedColumnName="stateCode"),
            @JoinColumn(name="departureStationNo",referencedColumnName="stationNo")
    })
    private Location departureLocation;

    @ManyToOne
    @JoinColumns({
               @JoinColumn(name="arrivalStateCode",referencedColumnName="stateCode"),
               @JoinColumn(name="arrivalStationNo",referencedColumnName="stationNo")
    })
    private Location arrivalLocation;

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    @Column(nullable = false)
    private LocalDate travelDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TravelTime travelTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatType seatType;
}