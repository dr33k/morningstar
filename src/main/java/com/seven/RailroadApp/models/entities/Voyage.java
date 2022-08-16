package com.seven.RailroadApp.models.entities;

import com.seven.RailroadApp.models.enums.VoyageStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity(name="voyage")
@Table(name="voyage")
@Data
public class Voyage {
    @Id
    @Column(nullable = false)
    private UUID voyageNo;

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
    private LocalDate travelDate;

    @Column(nullable = false)
    private LocalTime travelTime;

    @Column(nullable = true)
    private LocalDateTime arrivalDateTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VoyageStatus status;

    }