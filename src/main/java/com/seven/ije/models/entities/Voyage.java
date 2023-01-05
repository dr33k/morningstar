package com.seven.ije.models.entities;

import com.seven.ije.models.enums.VoyageStatus;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name="voyage")
@Table(name="voyage")
@Data
@ToString
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
    private LocalDateTime travelDateTime;

    @Column(nullable = true)
    private LocalDateTime arrivalDateTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VoyageStatus status;

    }